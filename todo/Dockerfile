# Stage 1: Build with Maven
# Use a base image with Maven and JDK 17, matching your project's Java version.
FROM maven:3.9-eclipse-temurin-17 AS build

# Set the working directory in the container
WORKDIR /app

# Install curl for the healthcheck and clean up apt-get cache to keep image small
RUN apt-get update && apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*
    
# Copy the Maven wrapper and project definition files.
# This allows us to leverage Docker's layer caching for dependencies.
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies first to optimize build time
RUN ./mvnw dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Build the application using the 'prod' profile.
# The -Pprod flag activates the production profile in pom.xml,
# which includes the PostgreSQL driver and sets the Spring profile to 'prod'.
RUN ./mvnw clean install -Pprod -DskipTests

# Stage 2: Create the final, lightweight runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/todo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]