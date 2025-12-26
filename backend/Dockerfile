#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]

