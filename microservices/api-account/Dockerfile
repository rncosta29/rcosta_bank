# Step 1: Using the base Java image
FROM openjdk:17-jdk-slim

# Step 2: Installing Maven and SonarQube Scanner
RUN apt-get update && apt-get install -y wget unzip maven curl
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.6.2.2472-linux.zip
RUN unzip sonar-scanner-cli-4.6.2.2472-linux.zip -d /opt
ENV PATH="/opt/sonar-scanner-4.6.2.2472-linux/bin:$PATH"

# Step 3: Setting the working directory inside the container
WORKDIR /app

# Step 4: Copying the source code into the container
COPY . /app

# Step 5: Running mvn clean install to compile the project
RUN mvn clean install -DskipTests

# Step 6: Copying the generated JAR file into the container
COPY target/api-account.jar api-account.jar

# Step 7: Copying the wait-for-it.sh script into the container
COPY wait-for-it.sh /app/wait-for-it.sh

# Step 8: Copying the start.sh initialization script into the container
COPY start.sh /app/start.sh

# Step 9: Ensuring the scripts are executable
RUN chmod +x /app/wait-for-it.sh /app/start.sh

# Step 10: Running sonar-scanner before starting the API
ENTRYPOINT ["sh", "-c", "sonar-scanner && /app/start.sh"]
