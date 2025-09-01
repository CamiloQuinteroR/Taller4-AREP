# Taller de Arquitecturas de Servidores de Aplicaciones, Meta protocolos de objetos, Patrón IoC, Reflexión

Para este taller se construyó un servidor Web (tipo Apache) en Java. El servidor es capaz de entregar páginas html e imágenes tipo PNG. Igualmente el servidor provee un framework IoC para la construcción de aplicaciones web a partir de POJOS. Usando el servidor se debe construyó una aplicación Web de ejemplo. El servidor atiende múltiples solicitudes no concurrentes.

Para este taller se dearrolló un prototipo mínimo que demuestre las capacidades reflexivas de JAVA y permite cargar un bean (POJO) y derivar una aplicación Web a partir de él. 


## Primeros pasos

Para ejecutar nuestro proyecto primero debemos clonar este repositorio, para esto nos debemos dirigir a la consola de nuestro equipo y clonar el proyecto siguiendo los pasos a continuación:

En nuestra consola, compiamos y ejecutamos la siguiente linea:

```
git clone https://github.com/CamiloQuinteroR/Taller3-AREP.git
```

<img width="798" height="166" alt="image" src="https://github.com/user-attachments/assets/1667478e-1129-45e9-9c7b-c64a8232bd7a" />


Al ejecutar este comando, ya tendremos el proyecto de forma local. 



### Prerequistos

Para ejecutar este proyecto deberás tener instalado en tu maquina cualquier IDE, como los son NetBeasn o Visual Studio Code.
En este caso, abriremos y ejecutaremos el poryecto usando NetBeans. 

Sin importar el IDE deberás tener instalado JDK 23 y Maven. 

Si deseas instalar Maven basta con dirigirnos a la página de Maven y descargar el instalador que requiera nuestra máquina:

```
https://maven.apache.org/download.cgi
```

Al descargar el archivo, seguiremos los pasos de la instalación, es realemente sencillo. 


### Instalando

Primero debemos compilar nuestro proyecto con el siguiente comando:

```
mvn compile
```

<img width="801" height="396" alt="image" src="https://github.com/user-attachments/assets/508d31c6-d1bf-44d8-9e65-ef60f00dca43" />


Despues de tener nuestro proyecto clonado de forma local y compilado. 

A continuación ejecutamos nuestro proyecto con el siguiente comando:

```
mvn exec:java
```

<img width="757" height="230" alt="image" src="https://github.com/user-attachments/assets/1ca9007e-6c9a-4123-b6b5-429c87f01a65" />

Veremos el mensaje inicial de nuestro servidor. 

También podemos ejecutar nuestro proyecto con el siguiente comando:

```
java -cp target/classes com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot
```

<img width="795" height="97" alt="image" src="https://github.com/user-attachments/assets/0420e234-a981-4a53-9006-4ebe8f8b3a2e" />


## Desarrollo

Se implementaron las interfaces GetMapping y RestController:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetMapping {
    public String value();
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RestController {}
```

En este caso la etiqueta @RestController marca una clase como controlador que debe ser detectada y @GetMapping marca un método como manejador de una ruta específica. 

Se implementó el primer controlador HelloController.java usando las etiquetas creadas:

```java
@RestController
public class HelloController {

    @GetMapping("/hello") 
    public static String index() { 
        return "Greetings from Spring Boot!"; 
    }
}
```

Para la primera versión se cargó el POJO desde la línea de comandos pasandola como parámetro cuando se invoca el framework:

```
java  -cp target/classes com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot com.mycompany.httpserver.examples.HelloController
```

<img width="793" height="59" alt="image" src="https://github.com/user-attachments/assets/911cc666-c3b0-49a8-9a65-fb2471be1070" />

Veremos el mensaje inicial de nuestro servidor. 

A continuación realizamos una prueba poniendo en el buscador la sigueinte URL:

```
http://localhost:35000/app/hello
```

<img width="348" height="166" alt="image" src="https://github.com/user-attachments/assets/a6111509-44ca-47db-920b-5cb57345254d" />

Podemos ver que el controlador funciona correctamente y la gestión de la etiqueta @GetMapping tambien. 

En la versión final el framework explora el directorio raiz buscando classes con la anotación  @RestController para indicar que son componentes y cargar respectivamente.  

Para esto se creó el siguiente método que busca todas las clases de un paquete, carga aquellas anotadas con @RestController, crea sus instancias y registra en un diccionario los métodos que tengan la anotación @GetMapping:

```java
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
```




Implementamos además el siguiente controlador:

```java
@RestController
public class GreetingController {
    
    @GetMapping("/greeting")
    public static String greeting(@RequestParam(value = "name", defaultValue = "mundo") String name) {
        return "Hola " + name;
    }
    
}
```
Para esto fue nuecesario crear la interfaz RequestParam.java para usar la anotación @RequestParam:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();
    String defaultValue() default "";
}
```
Ahora para probar su correcto funcionamiento, ejecutamos nuestro proyecto y escribimos en el browser la siguiente URL:

```
http://localhost:35000/app/greeting?name=Camilo
```

<img width="429" height="164" alt="image" src="https://github.com/user-attachments/assets/aa77f036-80a0-4a80-b326-6d8da70fb334" />



## Ejecución de las pruebas

Para ejecutar las pruebas usaremos el siguiente comando en consola:

```
mvn test
```

O bien, otro comando útil puede ser:

```
mvn clean install
```
Veremos las pruebas ejecutarse y podemos evidenciar si tenemos o no errrores:

<img width="799" height="351" alt="image" src="https://github.com/user-attachments/assets/1fbe7d4d-008a-4314-9204-a76835ae2a9f" />


### Desglose en pruebas integrales

Se desarrollaron los siguientes tests con le fin de verificar que las funcionalidades de nuestro proyecto y los controladores responden de forma correcta:

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



## Desarrollado con

* https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html - Java 17
* https://maven.apache.org/ - Maven

## Arquitectura



## Autor

* **Camilo Andrés Quintero Rodriguez**

