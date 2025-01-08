# Base image Jenkins LTS com Java 17
FROM jenkins/jenkins:lts-jdk17

# Instalando utilitários necessários
USER root
RUN apt-get update && \
    apt-get install -y \
    curl \
    git \
    docker.io \
    unzip \
    wget \
    gnupg2 \
    lsb-release \
    ca-certificates && \
    apt-get clean

# Configuração do Sonar-Scanner
RUN mkdir -p /opt/sonar-scanner && \
    curl -sSLo /opt/sonar-scanner/sonar-scanner-cli.zip https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.8.0.2856-linux.zip && \
    unzip /opt/sonar-scanner/sonar-scanner-cli.zip -d /opt/sonar-scanner && \
    rm /opt/sonar-scanner/sonar-scanner-cli.zip && \
    ln -s /opt/sonar-scanner/sonar-scanner-4.8.0.2856/bin/sonar-scanner /usr/local/bin/sonar-scanner

# Instalar plugins usando jenkins-plugin-cli
RUN jenkins-plugin-cli --plugins \
    sonar \
    git \
    docker-workflow \
    pipeline-github-lib \
    blueocean \
    maven-plugin \
    gradle \
    mysql \
    junit \
    jacoco \
    kubernetes-cli

# Copiar o arquivo config.xml personalizado para a pasta do Jenkins
COPY config.xml /var/jenkins_home/config.xml

# Configuração do ambiente
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH=$JAVA_HOME/bin:$PATH
ENV SONAR_SCANNER_HOME=/opt/sonar-scanner/sonar-scanner-4.8.0.2856

# Porta do Jenkins
EXPOSE 8080
EXPOSE 50000

# Volumes para persistência de dados
VOLUME /var/jenkins_home

# Definir o comando de inicialização do Jenkins
CMD ["java", "-jar", "/usr/share/jenkins/jenkins.war"]
