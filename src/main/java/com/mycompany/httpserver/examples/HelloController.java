/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.httpserver.examples;

import com.mycompany.httpserver.GetMapping;
import com.mycompany.httpserver.RestController;

/**
 *
 * @author CAMILO.QUINTERO-R
 */
@RestController
public class HelloController {

	@GetMapping("/Hello")
	public static String index() {
		return "Greetings from Spring Boot!";
	}
}
