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
	public void shouldReturnIndex() {
		String result = com.mycompany.httpserver.examples.HelloController.index();
		assertEquals("Greetings from Spring Boot!", result);
	}

	@Test
	public void shouldReturnGreetingWhitDefaultValue() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("mundo");
		assertEquals("Hola mundo", result);
	}

	@Test
	public void shouldReturnGreetingWhitValue() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("Milo");
		assertEquals("Hola Milo", result);
	}

	@Test
	public void shouldReturnGreetingEmpty() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("");
		assertEquals("Hola ", result);
	}
}
