# Banking Solution

This is a simple banking solution built using Spring Boot, providing a set of APIs for managing bank accounts and transactions such as deposits, withdrawals, and transfers.

## Prerequisites

Before running the application, make sure you have the following installed:

- Java 17 or higher
- Maven (for building the project)
- An IDE (optional, but recommended for development)

## Getting Started

### 1. Clone the Repository

Clone the project to your local machine using the following command:

```bash
git clone https://github.com/Libenko96/banking-solution.git
```
### 2. Build the Application

Navigate to the project directory and build the application using Maven:
```bash
cd banking-solution
mvn clean install
```

### 3. Run the Application
To start the application, use the following Maven command:
```bash
mvn spring-boot:run
```
The application will start on the default port 8080.

### 4. Accessing the H2 Console
You can access the H2 database console at http://localhost:8080/h2-console. The credentials for the database are:

#### JDBC URL: jdbc:h2:mem:banking
#### Username: sa
#### Password: (leave blank)

### 5. API Documentation (Swagger UI)
The project integrates Swagger UI for API documentation. To access it, open the following URL in your browser:
http://localhost:8080/swagger-ui.html
It provides an interactive interface to explore the APIs.

## Technologies Used

- **Spring Boot** for building the application
- **H2 Database** for the in-memory database
- **MapStruct** for object mapping
- **Swagger UI** for API documentation
- **JUnit** and **Mockito** for unit testing
- **Lombok** for reducing boilerplate code


