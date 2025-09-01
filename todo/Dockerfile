# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy only pom.xml first and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the actual project
COPY src ./src

# Build Spring Boot app (skip tests for faster CI/CD)
RUN mvn clean package -DskipTests

# ---- Stage 2: Runtime ----
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy JAR from the builder
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Force Spring to use prod profile
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]