# Bookington Backend

This is the backend service for the Bookington application, built with Java Spring Boot.

## Prerequisites

- Java 21
- Maven (or use the provided `mvnw` wrapper)
- PostgreSQL (ensure database is running and configured in `application.yaml`)

## Setup & Run

1.  Navigate to the `backend` directory:
    ```bash
    cd backend
    ```

2.  Configure database connection in `src/main/resources/application.yaml`.

3.  Run the application:
    ```bash
    ./mvnw spring-boot:run
    ```
    (On Windows PowerShell):
    ```powershell
    .\mvnw spring-boot:run
    ```

4.  The server will start on port 8080 (default).
    API Documentation (Swagger) will be available at: http://localhost:8080/swagger-ui.html
