# rcosta_bank Microservices Application

## Overview
The `rcosta_bank` project is a microservices-based application designed for managing banking-related services. The application is organized into distinct services for scalability and maintainability. It leverages modern monitoring tools to ensure high performance and reliability.

---

## Project Structure

The application consists of the following components:

```
rcosta_bank
|-- server
|-- gateway
|-- microservices
    |-- api-credit
    |-- api-account
```

### Components
- **Server**: The core backend responsible for managing the overall application lifecycle.
- **Gateway**: Acts as an entry point for external clients, routing requests to appropriate microservices.
- **Microservices**:
  - **api-credit**: Handles operations related to credit services, including credit applications and transactions.
  - **api-account**: Manages user accounts, including account creation, updates, and balance inquiries.

---

## Monitoring
To maintain observability and performance, the following tools are integrated:

### SonarQube
- Used for code quality analysis.
- Helps detect code smells, bugs, and vulnerabilities in the application.

### Grafana
- Provides real-time visualization and monitoring.
- Displays key performance metrics and logs for proactive system management.

---

## Getting Started

### Prerequisites
Ensure the following tools are installed on your system:
- [Docker](https://www.docker.com/)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [MySQL](https://www.mysql.com/)

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/rncosta29/rcosta_bank.git
   cd rcosta_bank
   ```

2. Build and run the Docker containers:
   ```bash
   sudo docker-compose --env-file .env-hml up -d --remove-orphans --build
   ```

3. Access the services:
   - Gateway: `http://192.168.15.15:8761`
   - API Documentation (e.g., Swagger): `http://192.168.15.15:8761/swagger-ui.html`

4. Access monitoring tools:
   - SonarQube: `http://192.168.15.13:9000`
   - Grafana: `http://192.168.15.13:3000`

---

## Future Enhancements
- Integration of ElasticSearch for enhanced observability.
- Additional microservices for new banking functionalities.

---

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch.
3. Make your changes and commit them.
4. Submit a pull request.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.