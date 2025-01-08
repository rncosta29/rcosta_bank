# Etapa 1: Usando a imagem base do Java
FROM openjdk:17-jdk-slim

# Etapa 2: Instalando o SonarQube Scanner
RUN apt-get update && apt-get install -y wget unzip
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.2.2472-linux.zip
RUN unzip sonar-scanner-cli-4.6.2.2472-linux.zip -d /opt
ENV PATH="/opt/sonar-scanner-4.6.2.2472-linux/bin:$PATH"

# Etapa 3: Configurando o diretório de trabalho dentro do container
WORKDIR /app

# Etapa 4: Copiando o JAR gerado do projeto para o container
COPY target/api-credit.jar api-credit.jar

# Etapa 5: Copiando o script wait-for-it.sh para dentro do container
COPY wait-for-it.sh /app/wait-for-it.sh

# Etapa 6: Copiando o script de inicialização start.sh para dentro do container
COPY start.sh /app/start.sh

# Etapa 7: Garantindo que os scripts sejam executáveis
RUN chmod +x /app/wait-for-it.sh /app/start.sh

# Etapa 8: Rodando o sonar-scanner antes de iniciar a API
CMD sonar-scanner -Dsonar.projectKey=rcosta_bank -Dsonar.projectName=rcosta_bank -Dsonar.projectVersion=1.0 -Dsonar.host.url=http://192.168.15.13:9000 && /app/start.sh
