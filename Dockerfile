# ================================
# FASE 1: Compilación
# ================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiamos primero solo el pom.xml y descargamos dependencias
# Si el código cambia pero no el pom, Docker usa la caché y no re-descarga, mucho más rápido
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el código y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B

# ================================
# FASE 2: Imagen final de producción
# ================================
FROM eclipse-temurin:21-jre-alpine AS production

WORKDIR /app

# Usuario no root por seguridad, nunca correr como root en producción
RUN addgroup -S slotly && adduser -S slotly -G slotly

# Copiamos solo el JAR generado en la fase 1
COPY --from=builder /app/target/*.jar app.jar

# El JAR pertenece al usuario slotly
RUN chown slotly:slotly app.jar

USER slotly

# Exponemos el puerto de producción
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]