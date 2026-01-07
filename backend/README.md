# Bookinton Backend API

This is the backend server for the Bookinton application, developed using **Java Spring Boot**.

## 🛠 Prerequisites

-   **Java 21** (JDK 21)
-   **Maven** (or use the included `mvnw`)
-   **PostgreSQL** Database

## 🚀 Getting Started

### 1. Database Configuration
Ensure your PostgreSQL database is running. Update the connection details in `src/main/resources/application.yaml` if necessary.

**Note:** The system uses `src/main/resources/data.sql` to initialize sample data (Roles, Users, Courts, Bookings) on startup.

### 2. Run the Application

Open your terminal in this directory (where `pom.xml` serves as the root).

**Using Maven Wrapper:**

*   **Linux/macOS:**
    ```bash
    ./mvnw spring-boot:run
    ```
*   **Windows (Command Prompt/PowerShell):**
    ```powershell
    .\mvnw spring-boot:run
    ```

### 3. API Documentation
Once the server is running, you can access the Swagger UI documentation at:
👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 📂 Project Structure

*   `src/main/java`: Source code
*   `src/main/resources`: Configuration files and SQL scripts (`data.sql`)
