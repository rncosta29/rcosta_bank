FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/server.jar server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Dspring.profiles.active=hml", "-jar", "server.jar"]
