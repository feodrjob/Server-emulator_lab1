FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# копируем собранный jar-файл нашего сервера внутрь контейнера
COPY target/*.jar app.jar

EXPOSE 8080

# команда для запуска
ENTRYPOINT ["java", "-jar", "app.jar"]