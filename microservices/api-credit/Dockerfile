FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/api-credit.jar api-credit.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Dspring.profiles.active=hml", "-jar", "api-credit.jar"]
