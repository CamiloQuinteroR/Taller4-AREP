# Taller de de modularización con virtualización e Introducción a Docker

El taller consistió en crear una aplicación web pequeña. Despues de desarrollar esta aplicación procedimos a construir un container para docker para la aplicación y los desplegaremos y configuraremos en nuestra máquina local. Luego, creamos un repositorio en DockerHub y subimos la imagen al repositorio. Finalmente, creamos una máquina virtual en AWS, instalamos Docker, y desplegamos el contenedor que acabamos de crear.

## Resumen del proyecto

El proyecto consiste en la implementación de un servidor HTTP en Java, inspirado en el funcionamiento de frameworks como Spring Boot, pero construido desde cero para comprender su lógica interna, el sistema busca ser una base para entender cómo funcionan los servidores web y los microframeworks en Java, ofreciendo tanto manejo de archivos estáticos como servicios dinámicos.

El servidor se ejecuta en el puerto 35000 y es capaz de:

- Atender múltiples solicitudes de manera concurrente, gracias al uso de hilos que permiten procesar cada cliente de forma independiente.

- Servir archivos estáticos (HTML, CSS, JS e imágenes) almacenados en un directorio designado por el usuario.

- Exponer servicios tipo REST, soportando rutas configuradas mediante anotaciones personalizadas (@RestController, @GetMapping).

- Procesar parámetros en la URL y responder en formato JSON, simulando la lógica de controladores en frameworks modernos.

- Cargar controladores automáticamente a partir de un paquete específico, registrando sus métodos en el mapa de servicios.

- Finalizar de forma controlada, mostrando un mensaje al apagarse cuando se detiene el proceso. 

## Primeros pasos

Para ejecutar nuestro proyecto primero debemos clonar este repositorio, para esto nos debemos dirigir a la consola de nuestro equipo y clonar el proyecto siguiendo los pasos a continuación:

En nuestra consola, copiamos y ejecutamos la siguiente línea:

```
git clone https://github.com/CamiloQuinteroR/Taller4-AREP.git
```
Al ejecutar este comando, ya tendremos el proyecto de forma local. 


### Prerrequisitos

Para ejecutar este proyecto deberás tener instalado en tu máquina cualquier IDE, como los son NetBeans o Visual Studio Code.
En este caso, abriremos y ejecutaremos el proyecto usando NetBeans. 

Sin importar el IDE deberás tener instalado JDK 17 y Maven. 

Si deseas instalar Maven basta con dirigirnos a la página de Maven y descargar el instalador que requiera nuestra máquina:

```
https://maven.apache.org/download.cgi
```

Al descargar el archivo, seguiremos los pasos de la instalación, es realmente sencillo. 


### Ejecutando de forma local

Primero debemos compilar nuestro proyecto con el siguiente comando:

```
mvn clean install
```

Después de tener nuestro proyecto clonado de forma local y compilado. 

A continuación ejecutamos nuestro proyecto con el siguiente comando:

```
mvn exec:java
```

Veremos el mensaje inicial de nuestro servidor "Listo para recibir ..."

También podemos ejecutar nuestro proyecto con el siguiente comando:

```
java -cp "target/classes:target/dependency/*" com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot

```

Una vez que el servidor está corriendo, podemos probar las siguientes rutas desde el navegador o con herramientas como `curl`:

```
http://localhost:35000
```
```
http://localhost:35000/app/hello
```
```
http://localhost:35000/app/greeting?name=Camilo
```
>  **Nota importante**  
> En el desarrollo se explicará cómo ejecutar el proyecto desde **contenedores Docker** y desde una **máquina virtual en AWS**.  
> 
> Los comandos mostrados anteriormente son útiles únicamente si se desea **clonar el proyecto y ejecutarlo de forma local** en el equipo de desarrollo.
 

## Desarrollo

### Docker

En esta sección se construyó la imagen Docker del proyecto, se levantaron varios contenedores a partir de ella para probar su ejecución en diferentes puertos, y finalmente se publicó la imagen en Docker Hub para poder reutilizarla o desplegarla en otros entornos.

#### Generando la imágen

1. Compilamos nuestro proyecto:

```
mvn clean package
```

2. Creamos la imagen en Docker:

```
docker build --tag dockersparkprimer .
```

4. Revisamos que la imagen fue construida:

```
docker images
```

#### Generando los contenedores

4. A partir de la imagen creada creemos tres instancias de un contenedor docker:


```
docker run -d -p 34000:35000 --name firstdockercontainer dockersparkprimer
docker run -d -p 34001:35000 --name firstdockercontainer2 dockersparkprimer
docker run -d -p 34002:35000 --name firstdockercontainer3 dockersparkprimer
```

5. Deberiamos ver las imagenes creadas y corriendo en Docker:

<img width="1055" height="392" alt="image" src="https://github.com/user-attachments/assets/fd189547-5f73-4c24-be0a-de23efe406ea" />

#### Docker Hub

6. En nuestro motor de docker local creamos una referencia a nuestra imagen con el nombre del repositorio a donde deseamos subirla:
```
docker tag dockersparkprimer [nombre del repositorio]
```
7. Empujamos la imagen al repositorio en DockerHub
```
docker push [nombre del repositorio]:latest
```
8. Despues de esto veremos esto o algo similar en nuestro respositorio:

<img width="769" height="561" alt="image" src="https://github.com/user-attachments/assets/dbc35947-0f29-43a6-8d46-b9446cf9aa8d" />

### AWS EC2

1. Nos conectamos a nuestra máquina por medio de SSH:

```
ssh -i "llavesAREP.pem" ec2-user@ec2-54-175-252-15.compute-1.amazonaws.com
```

2. Nos registramos en Docker Hub:

```
docker login
```

3. Corremos el contenedor desde la consola de nuestra máquina virtual:
```
docker run -d -p 42000:35000 --name taller4arep camiloquinteror/taller4-arep
```

4. Escribimos la siguiente URL en el navegado3 con la IP pública de nuestra instancia:
```
http://<IP-EC2>:42000 
```
Para las pruebas, la IP pública de la máquina era 54.175.252.15. 

## Ejecución de las pruebas

En esta sección se documentaron las pruebas de funcionamiento y las pruebas unitarias realizadas sobre el proyecto.

Primero, se validó el despliegue de la aplicación accediendo a diferentes URLs: la raíz para comprobar la carga de archivos estáticos, /app/hello para verificar la respuesta de un controlador simple, y /app/greeting?name= para confirmar el manejo de parámetros en la URL.

Posteriormente, se ejecutaron pruebas unitarias con Maven, comprobando que los controladores respondieran correctamente en distintos escenarios: el saludo por defecto, el uso de un valor específico como parámetro, y casos especiales como valores vacíos. Estas pruebas aseguran que la lógica de los controladores funciona de forma esperada y robusta.


### Pruebas de funcionalidades 

1. Probamos el despliegue de nuestra aplicacion con la siguiente URL, podemos ver que funciona correctamente, regresando los archivos estáticos de la página web:

```
http://54.175.252.15:42000 
```

<img width="1272" height="560" alt="image" src="https://github.com/user-attachments/assets/cc7c099a-4dcc-4598-8b00-4aac0bb9cb6b" />


2. Probamos la siguiente URL /hello, como podemos ver regresa el mensaje de forma correcta:

```
http://54.175.252.15:42000/app/hello
```
<img width="1035" height="372" alt="image" src="https://github.com/user-attachments/assets/b574148e-c551-44e5-b7a5-c1ae240909ad" />


3. Probamos la siguiente URL /greeting y un parámetro, como podemos ver regresa el mensaje de forma correcta:

```
http://54.175.252.15:42000/app/greeting?name=hello
```

<img width="735" height="266" alt="image" src="https://github.com/user-attachments/assets/b0228b7d-b18f-4ea8-9bb3-223f53f9ed30" />



### Pruebas unitarias

Para ejecutar las pruebas usaremos el siguiente comando en consola:

```
mvn test
```

O bien, otro comando útil puede ser:

```
mvn clean install
```
Veremos las pruebas ejecutarse y podemos evidenciar si tenemos o no errores.

### Desglose en pruebas unitarias

Se desarrollaron los siguientes tests con el fin de verificar que las funcionalidades de nuestro proyecto y los controladores responden de forma correcta:

Verifica que HelloController.index() retorne el saludo por defecto "Greetings from Spring Boot!"

```java
    @Test
	public void shouldReturnIndex() {
		String result = com.mycompany.httpserver.examples.HelloController.index();
		assertEquals("Greetings from Spring Boot!", result);
	}
```
Verifica que al pasar "mundo" como valor, GreetingController.greeting() retorne "Hola mundo":

```java
	@Test
	public void shouldReturnGreetingWhitDefaultValue() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("mundo");
		assertEquals("Hola mundo", result);
	}
```
Verifica que al pasar "Milo" como valor, el resultado sea "Hola Milo":

```java
	@Test
	public void shouldReturnGreetingWhitValue() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("Milo");
		assertEquals("Hola Milo", result);
	}
```
Verifica que al pasar una cadena vacía, el resultado sea "Hola ":

```java
	@Test
	public void shouldReturnGreetingEmpty() {
		String result = com.mycompany.httpserver.examples.GreetingController.greeting("");
		assertEquals("Hola ", result);
	}
```

## Arquitectura

La arquitectura de este proyecto sigue un diseño modular inspirado en frameworks como Spring Boot, pero implementado desde cero. El núcleo es la clase HttpServer, que gestiona las conexiones entrantes en el puerto 35000, atiende solicitudes concurrentes mediante hilos y resuelve rutas para servir archivos estáticos o ejecutar servicios REST definidos por anotaciones. La clase MicroSpringBoot actúa como el punto de arranque, encargándose de cargar dinámicamente los controladores desde un paquete específico, registrando los métodos anotados con @GetMapping dentro de un mapa de servicios en HttpServer. De esta forma, la arquitectura combina: un servidor HTTP ligero, un sistema de enrutamiento basado en anotaciones, y un manejador de archivos estáticos, ofreciendo una base sencilla pero funcional para comprender cómo trabajan los microframeworks web en Java.

## Diseño de Clases

### HttpServer
Clase principal que implementa el servidor HTTP. Atiende solicitudes, sirve archivos estáticos y resuelve rutas de servicios registrados.  
Utiliza un `Map<String, Method>` para mapear rutas a métodos.

### MicroSpringBoot
Punto de entrada (`main`). Escanea el paquete `examples`, registra controladores con anotaciones y arranca el `HttpServer`.

### Anotaciones
`@RestController`, `@GetMapping`, `@RequestParam`  
Simulan el estilo de Spring Boot, permitiendo marcar clases como controladores y métodos como endpoints.

### HttpRequest y HttpResponse
Clases que encapsulan la URI de la petición y la respuesta.  
- `HttpRequest` obtiene parámetros de la query.  
- `HttpResponse` actúa como base para construir respuestas.



## Desarrollado con

* https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html - Java 17
* https://maven.apache.org/ - Maven





## Autor

* **Camilo Andrés Quintero Rodriguez**

