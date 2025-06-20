# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY demo/pom.xml demo/pom.xml
COPY demo/src demo/src
RUN mvn -f demo/pom.xml clean package -DskipTests

# Stage 2: Run the application with full JDK (not headless)
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/demo/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
