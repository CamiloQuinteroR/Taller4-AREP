/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver.MicroSpringBoot;

import com.mycompany.httpserver.HttpServer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CAMILO.QUINTERO-R
 */
public class MicroSpringBoot {
    public static void main(String[] args) {
        try {
            HttpServer.staticFiles("webroot");
            HttpServer.startServer(args);
        } catch (IOException ex) {
            Logger.getLogger(MicroSpringBoot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MicroSpringBoot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
