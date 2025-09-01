package com.mycompany.httpserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que implementa un servidor http en un puerto dado respondinedo varias
 * solicitudes y llamando archivos
 *
 * @author Camilo Andrés Quintero Rodríguez
 */
public class HttpServer {

    /**
     * Inicia el servidor en el puerto 35000 y permite que soporte múlltiples
     * solicitudes seguidas no concurrentes
     *
     * @throws IOException Si ocurre un error de entrada/salida al crear el
     * socket.
     * @throws URISyntaxException Si ocurre un error con la URI solicitada.
     */
    public static Map<String, Method> services = new HashMap();
    private static String folder;

    public static void startServer(String[] args) throws IOException, URISyntaxException {
        
        //Documentamos loadComponents(arg) porque ahora es automática la lectura de componentes
        //loadComponents(args);
        ServerSocket serverSocket = null;
        try {
            // Creamos un servidor que escucha por el puerto 35000
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean running = true;
        // Bucle que nos permite solicitudes secuenciales
        while (running) {

            try {
                System.out.println("Listo para recibir ...");
                // Esperamos que se conecte el cliente
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            String path = null;
            URI requesturi = null;
            boolean firstline = true;
            while ((inputLine = in.readLine()) != null) {
                if (firstline) {
                    requesturi = new URI(inputLine.split(" ")[1]);
                    path = requesturi.getPath();
                    System.out.println("Path: " + path);
                    firstline = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            if (path == null || path.equals("/")) {
                path = "/index.html";
            }

            //piner otro if para que responda las consultas de la actividad anterior
            if (path.startsWith("/app/task")) {
                String response = taskService(requesturi);
                out.print(response);
                out.flush();
            } else if (path.startsWith("/app")) {
                String response = processRequest(requesturi);
                out.print(response);
                out.flush();
            } else {
                serveStaticFile(out, dataOut, path);
            }

            out.close();
            dataOut.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    /**
     * Retorna el tipo correspondiente a un archivo según su extensión.
     *
     * @param fileName Nombre del archivo solicitado
     * @return Cadena con el tipo de archivo
     */
    private static String tipoDeArchivo(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html";
        }
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        return "application/octet-stream";
    }

    /**
     * Genera una respuesta HTTP con contenido en formato JSON para un servicio
     * REST.
     *
     * @param requesturi Objeto URI que contiene la solicitud del cliente
     * @return Encabezados y cuerpo en JSON).
     */
    private static String taskService(URI requesturi) {
        String response = "HTTP/1.1 200 OK\n\r"
                + "Content-Type: application/json\n\r"
                + "\n\r";
        String name = "Mundo";
        if (requesturi.getQuery() != null && requesturi.getQuery().contains("=")) {
            name = requesturi.getQuery().split("=")[1];
        }
        response = response + "{\"mensaje\": \"Tarea " + name + "\"}";
        return response;
    }

    private static String processRequest(URI requesturi) {
        try {
            String serviceRoute = requesturi.getPath().substring(4);
            HttpRequest req = new HttpRequest(requesturi);
            String key = requesturi.getPath().substring(4);
            HttpResponse res = new HttpResponse(requesturi);
            Method m = services.get(key);

            String header = "HTTP/1.1 200 OK\n\r"
                    + "content-type: application/json\n\r"
                    + "\n\r";

            
            String name = null;
            String query = requesturi.getQuery();
            if (query != null && query.contains("name=")) {
                String[] params = query.split("&");
                for (String param : params) {
                    if (param.startsWith("name=")) {
                        name = param.substring(5);
                        break;
                    }
                }
            }

           
            if (m.getParameterCount() == 1) {
                return header + m.invoke(null, name);
            } else {
                return header + m.invoke(null);
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "404";
    }
   

    private static void serveStaticFile(PrintWriter out, BufferedOutputStream dataOut, String path) throws IOException {
        File file = new File(System.getProperty("user.dir"), "www/" + folder + path);

        if (file.exists() && !file.isDirectory()) {
            byte[] fileData = Files.readAllBytes(file.toPath());

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + tipoDeArchivo(file.getName()));
            out.println("Content-Length: " + fileData.length);
            out.println();
            out.flush();

            dataOut.write(fileData, 0, fileData.length);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<h1>404 Not Found</h1>");
        }
    }

    public static void staticFiles(String path) {
        folder = path;
    }

    private static void loadComponents(String[] args) {
        try {
            Class c = Class.forName(args[0]);
            if (c.isAnnotationPresent(RestController.class)) {
                Method[] methods = c.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.isAnnotationPresent(GetMapping.class)) {
                        String mapping = m.getAnnotation(GetMapping.class).value();
                        services.put(mapping, m);
                    }
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
