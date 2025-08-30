/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

public class Tests {
	@Test
	public void testHelloControllerIndex() {
		String result = com.mycompany.httpserver.examples.HelloController.index();
		assertEquals("Greetings from Spring Boot!", result);
	}

	@Test
	public void testHelloControllerGreetingDefault() {
		String result = com.mycompany.httpserver.examples.HelloController.greeting("mundo");
		assertEquals("Hola mundo", result);
	}

	@Test
	public void testHelloControllerGreetingCustom() {
		String result = com.mycompany.httpserver.examples.HelloController.greeting("Milo");
		assertEquals("Hola Milo", result);
	}

	@Test
	public void testHelloControllerGreetingEmpty() {
		String result = com.mycompany.httpserver.examples.HelloController.greeting("");
		assertEquals("Hola ", result);
	}
}
