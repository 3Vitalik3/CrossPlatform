# syntax=docker/dockerfile:1

# Этап 1: сборка Spring Boot приложения.
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

# Отдельное копирование pom.xml позволяет Docker кэшировать зависимости.
COPY pom.xml ./
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package

# Этап 2: минимальный образ только с Java Runtime.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring \
    && adduser -S spring -G spring \
    && mkdir -p /app/data \
    && chown -R spring:spring /app

COPY --from=build --chown=spring:spring /workspace/target/vicemods-1.0.0.jar app.jar

USER spring:spring
EXPOSE 8080

# JVM автоматически читает JAVA_TOOL_OPTIONS. Значение можно заменить при запуске.
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/urandom"

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --quiet --spider http://localhost:8080/ || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
