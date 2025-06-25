# Etapa 1: Build of aplicación
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn clean install
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final ligera
FROM eclipse-temurin:21-jdk
WORKDIR /usr/src/app/
COPY --from=builder /app/target/*.jar /usr/src/app/app.jar
EXPOSE 8443
CMD ["java", "-jar", "app.jar"]
