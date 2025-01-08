# Etapa 1: Usando a imagem base oficial do Jenkins
FROM jenkins/jenkins:lts

# Etapa 2: Configurar Jenkins como usuario root para instalar ferramentas adicionais
USER root

# Etapa 3: Instalar Docker e Maven para execução das builds
RUN apt-get update && apt-get install -y \
    docker.io \
    maven \
    curl \
    unzip \
    wget

# Etapa 4: Instalar o Sonar Scanner para análise de codigo
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.2.2472-linux.zip && \
    unzip sonar-scanner-cli-4.6.2.2472-linux.zip -d /opt && \
    rm sonar-scanner-cli-4.6.2.2472-linux.zip
ENV PATH="/opt/sonar-scanner-4.6.2.2472-linux/bin:$PATH"

# Etapa 5: Configurar permissoes para Jenkins acessar o Docker
RUN usermod -aG docker jenkins

# Etapa 6: Voltar para o usuario Jenkins
USER jenkins

# Etapa 7: Expondo a porta do Jenkins
EXPOSE 8080
