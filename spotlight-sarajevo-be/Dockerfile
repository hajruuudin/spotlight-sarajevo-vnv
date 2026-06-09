FROM ubuntu:latest
LABEL authors="hajrudin.imamovic"

# Use OpenJDK 21 and make the application with all the JDK 21 essential tools
FROM eclipse-temurin:21-jdk-jammy

# Set working directory to /app, so all files are locate here
WORKDIR /app

# Copy Maven wrapper and project files from the java project.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the app with a standard maven clean command
RUN ./mvnw clean package -DskipTests

# Expose the port that Render assigns
EXPOSE 8080

# Set the start command
CMD ["java", "-jar", "target/spotlight-sarajevo-0.0.1-SNAPSHOT.jar"]