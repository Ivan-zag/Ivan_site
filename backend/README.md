# Проект Сайта храма Архангела-Михаила

# Архитектура Spring Boot приложения для управления сайтом церкви

## Модели данных (Сущности)

### `WorshipSchedule` (Расписание богослужений)
- **Поля**:
  - `id: Long` (первичный ключ)
  - `title: String` (название расписания)
  - `content: String` (HTML-контент расписания)
  - `createdAt: LocalDateTime`
  - `updatedAt: LocalDateTime`
- **Назначение**: Хранение расписания, сконвертированного из Word-файла

### `News` (Новости)
- **Поля**:
  - `id: Long`
  - `title: String` (заголовок)
  - `content: String` (текст новости)
  - `imageUrl: String` (путь к изображению)
  - `isActive: boolean` (флаг публикации)
  - `createdAt: LocalDateTime`
- **Назначение**: Управление новостями и их статусом

### `Photo` (Фотографии)
- **Поля**:
  - `id: Long`
  - `description: String` (описание)
  - `filePath: String` (путь к файлу)
  - `category: String` (категория, например "мероприятия")
  - `uploadedAt: LocalDateTime`
- **Назначение**: Хранение фотографий для галереи или новостей

### `User` (Пользователи)
- **Поля**:
  - `id: Long`
  - `username: String` (логин)
  - `password: String` (шифрованный пароль)
  - `role: Role` (роль: ADMIN, EDITOR)
- **Назначение**: Аутентификация и управление правами

---

## Контроллеры

### `ScheduleController`
- **Эндпоинты**:
  - `POST /api/schedule/upload`  
    Загрузка Word-файла, конвертация в HTML (Apache POI), сохранение в БД
  - `GET /api/schedule/{id}`  
    Получение HTML-расписания для отображения
  - `PUT /api/schedule/{id}`  
    Редактирование контента расписания

### `NewsController`
- **Эндпоинты**:
  - `POST /api/news`  
    Создание новости (с загрузкой изображения через `MultipartFile`)
  - `GET /api/news`  
    Получение списка активных новостей
  - `PUT /api/news/{id}`  
    Обновление текста/статуса новости
  - `DELETE /api/news/{id}`  
    Удаление новости

### `PhotoController`
- **Эндпоинты**:
  - `POST /api/photo/upload`  
    Загрузка фотографии (сохранение в файловой системе/S3)
  - `GET /api/photos?category={category}`  
    Получение фотографий по категории
  - `DELETE /api/photo/{id}`  
    Удаление фотографии

### `AuthController`
- **Эндпоинты**:
  - `POST /api/login`  
    Аутентификация пользователя (JWT-токен)
  - `POST /api/register`  
    Регистрация новых редакторов (только для ADMIN)

---

## Сервисы
- **`FileStorageService`**  
  Сохранение загруженных файлов в `/uploads`
- **`WordToHtmlConverter`**  
  Конвертация DOCX в HTML с использованием Apache POI
- **`NewsService`**  
  Управление статусом новостей (публикация/архивация)
- **`ImageService`**  
  Ресайз и оптимизация изображений (Thumbnailator)

---

## База данных (PostgreSQL)
```sql
CREATE TABLE worship_schedule (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE news (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    image_url VARCHAR(512),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    description TEXT,
    file_path VARCHAR(512) NOT NULL,
    category VARCHAR(100),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);


Защита данных
Spring Security
Ролевая модель доступа (ADMIN > EDITOR)
Валидация
Проверка форматов файлов (.docx, .jpg, .png)
Резервное копирование
Ежедневный экспорт БД через pg_dump