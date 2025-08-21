/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.web;

import static com.mycompany.httpserver.HttpServer.get;
import static com.mycompany.httpserver.HttpServer.startServer;
import static com.mycompany.httpserver.HttpServer.staticFiles;

/**
 *
 * @author Milo
 */
public class WebApplication {
    
    public static void main(String[] args) throws Exception {
        staticFiles("/webroot");
        
        //Método estático GET para servicios REST
        get("/hello", (req, resp) -> "Hello world!");
        
        //Mecanismo de extracción de valor de consulta
        get("/helloWhit", (req, resp) -> "Hello " + req.getValue("name"));
        
        //Especificación de la ubicación de archivos estáticos
        staticFiles("/www");
        
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });
        
        startServer(args);
    }
    
    
}
