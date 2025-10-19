# Etapa 1: Build
FROM maven:3.8.7-openjdk-8 AS build

WORKDIR /app

# Copiar archivos de proyecto
COPY pom.xml .
COPY src ./src

# Ejecutar build sin tests
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:8-jdk-alpine

WORKDIR /app

# Copiar el jar construido
COPY --from=build /app/target/*.jar app.jar

# Puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java","-jar","app.jar"]
