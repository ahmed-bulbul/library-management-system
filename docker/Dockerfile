


## Stage 1: Build with Maven
#FROM maven:3.9.6-eclipse-temurin-17 AS builder
#
#WORKDIR /app
#
## Copy pom.xml and download dependencies
#COPY ../pom.xml .
#RUN mvn dependency:go-offline
#
## Copy source code
#COPY ../src ./src
#
## Package the application
#RUN mvn clean package -DskipTests
#
## Stage 2: Run the application
#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#COPY --from=builder /app/target/*.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "app.jar"]




FROM openjdk:17-jdk-slim

LABEL maintainer="Library Management System"
LABEL version="1.0.0"

# Create app directory
WORKDIR /app

# Copy the jar file
COPY target/library-management-system-*.jar app.jar

# Expose port
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
