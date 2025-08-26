# Taller de Arquitecturas de Servidores de Aplicaciones, Meta protocolos de objetos, Patrón IoC, Reflexión

java  -cp target/classes com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot com.mycompany.httpserver.examples.HelloController
## Primeros pasos

Para ejecutar nuestro proyecto primero debemos clonar este repositorio, para esto nos debemos dirigir a la consola de nuestro equipo y clonar el proyecto siguiendo los pasos a continuación:

En nuestra consola, compiamos y ejecutamos la siguiente linea:

```
git clone https://github.com/CamiloQuinteroR/Taller-2-AREP.git
```

<img width="811" height="167" alt="image" src="https://github.com/user-attachments/assets/ef129645-4a58-4859-aaab-c323bb2f30da" />

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

Primero debemos compilar nuestro poryecto con el siguiente comando:

```
mvn compile
```

<img width="908" height="393" alt="image" src="https://github.com/user-attachments/assets/a0ec77d1-1ca4-42c8-8dd0-58fdbe6a8b35" />

Depsues de tener nuestro poryecto clonado de forma local y compilado, abrimos nuestro proyecto en el IDE de preferencia, en este caso los abriremos en NetBeans:

<img width="367" height="347" alt="image" src="https://github.com/user-attachments/assets/67d8db5a-d5a1-4461-9687-e12e212d5073" />

Nos dirigimos a la clase WebApplication.java:

<img width="333" height="234" alt="image" src="https://github.com/user-attachments/assets/0b698299-3f70-4e70-a29c-fb4148f09690" />


A continuación ejecutamos la clase WebApplication.java:

(Si estamos en NetBeans, basta con dar clic derecho sobre la clase y dar clic sobre la opción "Run File")

<img width="416" height="276" alt="image" src="https://github.com/user-attachments/assets/1c365fa9-4414-497f-af3c-61f3b8a5461e" />


Al ejecutar nuestro proyecto veremos en consola el mensaje incial de nuestro servidor:

<img width="676" height="232" alt="image" src="https://github.com/user-attachments/assets/a24458b8-920d-49ad-b1f5-f396bf09a05f" />



Si deseamos, tambien podemos ejecutar nuestro proyecto desde consola, para esto ejecutaremos el siguiente comando:


Hay que tener en cuenta que debemos ejecutar este comando en el directorio donde se encuentra ubicado nuestro archivo pom.xml. 

```
mvn exec:java
```

<img width="758" height="196" alt="image" src="https://github.com/user-attachments/assets/fd91cda3-dde8-480e-a198-1d3cea8a4f46" />


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


Como se puede evidenciar a continuación, el código también responde a solicitudes de archivos estáticos:

```
http://localhost:35000/index.html
```
<img width="568" height="607" alt="image" src="https://github.com/user-attachments/assets/e04b5d3e-ce32-47e9-a739-ff3c5e3fb36c" />



## Desarrollado con

* https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html - Java 23
* https://maven.apache.org/ - Maven

## Arquitectura

La arquitectura de este proyecto se basa en un servidor HTTP escrito en Java, que corre sobre sockets en el puerto 35000 y permite manejar tanto archivos estáticos como servicios REST sencillos. El núcleo lo constituye la clase HttpServer, que se encarga de escuchar solicitudes entrantes, interpretar la URI, y decidir si debe servir un recurso estático desde la carpeta configurada (staticFiles) o invocar un servicio registrado. Para la lógica de negocio se define la interfaz Service, implementada mediante funciones lambda en la clase WebApplication, que registra rutas (get) y ejecuta servicios en respuesta a las solicitudes. El flujo básico es: el servidor recibe una petición, crea objetos HttpRequest y HttpResponse para manejar parámetros y construir respuestas, busca el servicio asociado en el mapa de rutas o bien entrega el archivo estático, retorna al cliente la respuesta HTTP con encabezados y contenido. Esta arquitectur separa el manejo de peticiones, la definición de servicios REST y la gestión de archivos, lo que facilita la extensión del servidor con nuevas funcionalidades.

## Autor

* **Camilo Andrés Quintero Rodriguez**

