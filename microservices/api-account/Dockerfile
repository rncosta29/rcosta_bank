FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/api-account.jar api-account.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Dspring.profiles.active=hml", "-jar", "api-account.jar"]
