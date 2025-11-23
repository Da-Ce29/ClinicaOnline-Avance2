# Etapa 1: Build
FROM maven:3.9.3-eclipse-temurin-8 AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de proyecto
COPY pom.xml .
COPY src ./src

# Ejecutar build sin tests
RUN mvn clean package -Dmaven.test.skip=true

# Etapa 2: Runtime
FROM eclipse-temurin:8-jdk

# Directorio de trabajo
WORKDIR /app

# Copiar el jar construido desde la etapa build
COPY --from=build /app/target/*.jar app.jar

# Puerto que expondrá la app
EXPOSE 8080

# Configuración de memoria JVM para Render
ENV JAVA_OPTS="-Xmx384m -Xms128m"

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
