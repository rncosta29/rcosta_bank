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
COPY target/gateway.jar gateway.jar

# Etapa 7: Copiando o script wait-for-it.sh para dentro do container
COPY wait-for-it.sh /app/wait-for-it.sh

# Etapa 8: Copiando o script de inicialização start.sh para dentro do container
COPY start.sh /app/start.sh

# Etapa 9: Garantindo que os scripts sejam executáveis
RUN chmod +x /app/wait-for-it.sh /app/start.sh

# Expondo a porta
EXPOSE 8765

# Etapa 10: Configurando o comando para inicialização do container
CMD ["/app/start.sh"]
