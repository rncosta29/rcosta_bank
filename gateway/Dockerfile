FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gateway.jar gateway.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Dspring.profiles.active=hml", "-jar", "gateway.jar"]
