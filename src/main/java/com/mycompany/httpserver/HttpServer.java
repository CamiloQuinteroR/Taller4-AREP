package com.mycompany.httpserver;

import java.net.*;
import java.io.*;
import java.nio.file.*;


/**
 * Clase que implementa un servidor http en un puerto dado
 * respondinedo varias solicitudes y llamando archivos
 * 
 * @author Camilo Andrés Quintero Rodríguez
 */
public class HttpServer {

    
    /**
    * Inicia el servidor en el puerto 35000 y permite que soporte múlltiples solicitudes seguidas no concurrentes
    * @throws IOException Si ocurre un error de entrada/salida al crear el socket.
    * @throws URISyntaxException Si ocurre un error con la URI solicitada.
    */
    public static void main(String[] args) throws IOException, URISyntaxException {
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

            // Si la petición empieza con "/app/task", se responde con un JSON
            if (path.startsWith("/app/task")) {
                String response = taskService(requesturi);
                out.print(response);
                out.flush();
            } else {
                // Si no es un servicio, se busca el archivo en la carpeta "www"
                File file = new File("www" + path);
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

            out.close();
            dataOut.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    
    /**
     * Retorna el tipo correspondiente a un archivo según su extensión.
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
     * Genera una respuesta HTTP con contenido en formato JSON para un servicio REST.
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
}
