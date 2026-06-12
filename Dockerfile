# === ЭТАП 1: Сборка проекта ===
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Копируем файл настроек и скачиваем зависимости (кэширует интернет-трафик)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходный код и собираем .jar файл (без тестов для скорости)
COPY src ./src
RUN mvn clean package -DskipTests

# === ЭТАП 2: Запуск приложения ===
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Забираем только готовый .jar файл из первого этапа
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]