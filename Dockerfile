
FROM openjdk:17

WORKDIR /usrapp/bin

ENV PORT 35000
EXPOSE 35000

COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

COPY www /usrapp/bin/www


CMD ["java", "-cp", "./classes:./dependency/*", "com.mycompany.httpserver.MicroSpringBoot.MicroSpringBoot"]

