# === Build stage ===
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Work from /app
WORKDIR /app

# Copy pom and sources
COPY pom.xml .
COPY src ./src

# Build the app (Maven inside container)
RUN mvn -q -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -DskipTests package

# === Run stage ===
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render will inject PORT env var – Spring Boot uses it from application.properties
ENV PORT=8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
