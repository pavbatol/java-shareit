FROM amazoncorretto:11
WORKDIR /app
#COPY target/shareit-gateway-0.0.1-SNAPSHOT.jar app.jar
COPY target/*.jar app.jar
ENTRYPOINT("java", "-jar", "/app/app.jar")