# Etapa 1: Usando a imagem base do Java
FROM openjdk:17-jdk-slim

# Etapa 2: Instalando o Maven
RUN apt-get update && apt-get install -y maven

# Etapa 3: Configurando o diretório de trabalho dentro do container
WORKDIR /app

# Etapa 4: Copiando o código fonte para o container
COPY . /app

# Etapa 5: Executando mvn clean install para compilar o projeto
RUN mvn clean install -DskipTests

# Etapa 6: Copiando o JAR gerado do projeto para o container
COPY target/server.jar server.jar

# Expondo a porta
EXPOSE 8761

# Etapa 7: Configurando o comando para inicialização do container
ENTRYPOINT ["java", "-Dspring.profiles.active=hml", "-jar", "server.jar"]
