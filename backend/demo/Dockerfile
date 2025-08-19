# Этап сборки
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Копируем все файлы проекта
COPY . .

# Устанавливаем права на выполнение для mvnw
RUN chmod +x mvnw

# Загружаем зависимости и собираем проект
RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre

# Устанавливаем SQLite (если требуется)
RUN apt-get update && \
    apt-get install -y sqlite3 libsqlite3-dev

WORKDIR /app

# Создаем директорию для базы данных
RUN mkdir -p /app/database

# Копируем собранный jar
COPY --from=builder /app/target/*.jar app.jar

# Настраиваем права доступа
RUN chmod -R 777 /app/database

EXPOSE 8080

# Команда запуска
CMD java -jar app.jar



