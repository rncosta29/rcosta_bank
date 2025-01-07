# Etapa 1: Usando a imagem base do Java
FROM openjdk:17-jdk-slim

# Etapa 2: Configurando o diretório de trabalho dentro do container
WORKDIR /app

# Etapa 3: Copiando o JAR gerado do projeto para o container
COPY target/api-account.jar api-account.jar

# Etapa 4: Copiando o script wait-for-it.sh para dentro do container
COPY wait-for-it.sh /app/wait-for-it.sh

# Etapa 5: Copiando o script de inicialização start.sh para dentro do container
COPY start.sh /app/start.sh

# Etapa 6: Garantindo que os scripts sejam executáveis
RUN chmod +x /app/wait-for-it.sh /app/start.sh

# Porta
EXPOSE 8765

# Etapa 7: Configurando o comando para inicialização do container
CMD ["/app/start.sh"]
