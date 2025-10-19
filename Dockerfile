# Etapa 1: Build
FROM maven:3.9.3-eclipse-temurin-8 AS build

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

# Configurar memoria JVM para Render
ENV JAVA_OPTS="-Xmx384m -Xms128m"

# Comando para ejecutar la app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
