/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver.MicroSpringBoot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.httpserver.HttpServer;

/**
 *
 * @author CAMILO.QUINTERO-R
 */
public class MicroSpringBoot {
    
    private static void loadControllers(String paquete) throws Exception {
        String rutaPaquete = paquete.replace(".", "/");
        ClassLoader cargador = Thread.currentThread().getContextClassLoader();
        java.net.URL url = cargador.getResource(rutaPaquete);
        java.io.File directorio = new java.io.File(url.toURI());
        for (java.io.File archivoClase : java.util.Objects.requireNonNull(directorio.listFiles())) {
            if (archivoClase.getName().endsWith(".class")) {
                String nombreCompletoClase = paquete + "." + archivoClase.getName().replace(".class", "");
                Class<?> clase = Class.forName(nombreCompletoClase);
                if (clase.isAnnotationPresent(com.mycompany.httpserver.RestController.class)) {
                    Object instanciaControlador = clase.getDeclaredConstructor().newInstance();
                    // Registrar métodos con @GetMapping en el diccionario services
                    java.lang.reflect.Method[] metodos = clase.getDeclaredMethods();
                    for (java.lang.reflect.Method metodo : metodos) {
                        if (metodo.isAnnotationPresent(com.mycompany.httpserver.GetMapping.class)) {
                            String ruta = metodo.getAnnotation(com.mycompany.httpserver.GetMapping.class).value();
                            com.mycompany.httpserver.HttpServer.services.put(ruta, metodo);
                        }
                    }
                    System.out.println("Controlador encontrado: " + clase.getName());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            loadControllers("com.mycompany.httpserver.examples");
            HttpServer.staticFiles("webroot");
            HttpServer.startServer(args);
        } catch (IOException ex) {
            Logger.getLogger(MicroSpringBoot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MicroSpringBoot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
