
FROM openjdk:17

# Directorio de trabajo dentro del contenedor
WORKDIR /usrapp/bin

# Puerto en el que corre tu servidor
ENV PORT 35000
EXPOSE 35000

# Copiamos clases compiladas y dependencias
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

COPY www /usrapp/bin/www


# Comando de arranque
CMD ["java", "-cp", "./classes:./dependency/*", "com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot"]

