# Etapa 1: Build
FROM maven:3.8.8-openjdk-8 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de Maven y proyecto
COPY pom.xml .
COPY src ./src

# Limpiar y compilar el proyecto sin tests
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:8-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Copiar el jar compilado desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Puerto que expondrá la app
EXPOSE 8080

# Configurar memoria JVM para Render
ENV JAVA_OPTS="-Xmx384m -Xms128m"

# Comando para ejecutar la app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
