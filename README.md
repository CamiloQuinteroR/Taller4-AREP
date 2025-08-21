# Declaración del proyecto: Desarrollo de un marco web para servicios REST y gestión de archivos estáticos

En este taller se explorará la arquitectura de las aplicaciones distribuidas. Concretamente, exploraremos la arquitectura de  los servidores web y el protocolo http sobre el que están soportados. 

El reto consistió en diseñar e implementar un servidor web que soporta múlltiples solicitudes seguidas no concurrentes. El servidor es capaz de leer los archivos del disco local y retornar todos los archivos solicitados, incluyendo páginas html, archivos java script, css e imágenes. 
Además, se contruyó una pequeña aplicación web con  javascript, css, e imágenes para probar el servidor. Así mismo se incluyó en la aplicación la comunicación asíncrona con unos servicios REST en el backend.

## Primeros pasos

Para ejecutar nuestro proyecto primero debemos clonar este repositorio, para esto nos debemos dirigir a la consola de nuestro equipo y clonar el proyecto siguiendo los pasos a continuación:

En nuestra consola, compiamos y ejecutamos la siguiente linea:

```
git clone https://github.com/CamiloQuinteroR/Dise-o-y-estructuraci-n-de-aplicaciones-distribuidas.git
```

<img width="1170" height="166" alt="image" src="https://github.com/user-attachments/assets/c29b23f5-7738-4753-8551-11722f1823c4" />

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

Depsues de tener nuestro poryecto clonado de forma local, abrimos nuestro proyecto en el IDE de preferencia, en este caso los abriremos en NetBeans:

<img width="251" height="107" alt="image" src="https://github.com/user-attachments/assets/83c2326f-c74d-4fd5-aa1b-e231b0218896" />

Nos dirigimos a la clase principal, en este caso es HttpServer.java:

<img width="284" height="214" alt="image" src="https://github.com/user-attachments/assets/3aa96a41-3161-4c5e-ad1f-dabe86fbb708" />


A continuación ejecutamos la clase HttpServer.java:

(Si estamos en NetBeans, basta con dar clic derecho sobre la clase y dar clic sobre la opción "Run File")

<img width="293" height="304" alt="image" src="https://github.com/user-attachments/assets/4ef0931b-799e-46c9-902a-cad335653b28" />


Al ejecutar nuestro proyecto veremos en consola el mensaje incial de nuestro servidor:

<img width="590" height="267" alt="image" src="https://github.com/user-attachments/assets/0f75c8fc-7f5a-42f5-bf59-f3982b52b14c" />


Si deseamos, tambien podemos ejecutar nuestro proyecto desde consola, para esto ejecutaremos el siguiente comando:


```
mvn compile
```

<img width="1253" height="392" alt="image" src="https://github.com/user-attachments/assets/6fc2d9eb-bcb1-4689-b66d-dc1f2973f1b3" />


Hay que tener en cuenta que debemos ejecutar este comando en el directorio donde se encuentra ubicado nuestro archivo pom.xml. 

Posteriormente ejecutaremos el siguiente comando:

```
mvn compile
```

<img width="957" height="194" alt="image" src="https://github.com/user-attachments/assets/f8afbba4-5fe5-4c82-a0c8-29397242e606" />

Veremos en consola el mensaje inicial de nuestro servidor. 

## Ejecución de las pruebas

1. Método estático GET para servicios REST:

En este caso se implementó un get() método que permite a los desarrolladores definir servicios REST utilizando funciones lambda.

En este caso, la función implementada fue la siguiente:

```
get("/hello", (req, res) -> "hello world!");
```

Como podemos observar si usamos la sigueinte URL:

```
http://localhost:35000/app/hello
```

Obtenemos:

```
"Hello world!"
```

Como se puede evidenciar en la imagen:

<img width="426" height="180" alt="image" src="https://github.com/user-attachments/assets/bf9e73f1-118e-4fc8-ac20-ad634f5ed248" />


2. Mecanismo de extracción de valor de consulta:

En este caso se desarrolló un mecanismo para extraer parámetros de consulta de las solicitudes entrantes y hacerlos accesibles dentro de los servicios REST.

La función que se implementó fue la siguiete:

```
get("/hello", (req, res) -> "hello " + req.getValues("name"));
```

Como podemos observar si usamos la sigueinte URL:

```
http://localhost:35000/app/helloWhit?name{}
```

Obtenemos:

```
"Hello {name}"
```

Como se puede evidenciar en la imagen:

<img width="460" height="182" alt="image" src="https://github.com/user-attachments/assets/ea2b46fa-8a0c-4b6e-b8dd-dfd28819482a" />


3. Especificación de la ubicación de archivos estáticos:

En este caso se introdujo un staticfiles() método que permite a los desarrolladores definir la carpeta donde se encuentran los archivos estáticos.

Se imeplementó la siguiente función, teniendo en cuenta que los archivos estáticos se encuentran en webroot:

```
staticfiles("/webroot");
```

Como podemos observar si usamos la sigueinte URL:

```
http://localhost:35000
```

Obtenemos:

<img width="471" height="643" alt="image" src="https://github.com/user-attachments/assets/54d69a17-5517-4544-b601-f4ec2978fdcf" />


## Desarrollado con

* https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html - Java 23
* https://maven.apache.org/ - Maven

## Arquitectura

Para la implementación de esta solución, usamos una arquitectura cliente servidor, en este caso, el cliente lo podemos ver como la interfaz que interactúa con el usuario, mientas que el servidor es quien maneja las solicitudes y peticiones, encargándose de entregar los recursos necesarios para responder las solicitudes y los servicios REST. 

La arquitectura está compuesta por algunos componentes, usuario y pagina web. El usuario es quien interactúa con la aplicación y el navegador es el encargado de convertir y tratar los archivos html, css y js para renderizar su contenido y además, envía peticiones y recibe las respuestas del servidor HTTP.

Podemos decir que, los componentes clientes de nuestra arquitectura son los descritos anteriormente, es decir, el usuario y la pagina web, y así mismo, tenemos otros componentes como el servidor, quien recibe las solicitudes del navegador, entrega recursos como html, css, js e imágenes. Además del servidor tenemos los archivos estáticos descritos anteriormente que el servidor lee del disco y son enviados al navegador. 
Además de estos componentes tenemos el servicio REST /app/task encargado de exponer un servicio que recibe una solicitud y genera un JSON.  

En nuestra arquitectura, el flujo de comunicación entre los componentes es sencillo, pues el usuario manipula la pagina web, en este caso llenando el formulario, el navegador procede a enviar una solicitud al servidor y el servidor, decide que hacer, si se trata de una solicitud de servicio web, únicamente, se encarga de enviar los archivos necesarios que se encuentran en el disco, de lo contrario, si es un servicio REST, procesa la solicitud y le envía al navegador un JSON que es renderizado posteriormente por el mismo navegador. 


## Autor

* **Camilo Andrés Quintero Rodriguez**

