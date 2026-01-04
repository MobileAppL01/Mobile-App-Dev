# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre
WORKDIR /app
<<<<<<< HEAD
COPY --from=build /app/target/*.jar app.jar
=======

COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar demo.jar

# Expose port
>>>>>>> 31d7085b101d636c99787d67874a8faaa3769871
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
