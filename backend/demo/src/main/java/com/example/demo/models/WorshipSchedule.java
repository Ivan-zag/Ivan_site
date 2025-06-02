package com.example.demo.models;

import java.time.LocalDateTime;

public class WorshipSchedule {
    private Long id; // Первичный ключ
    private String title; // Название расписания
    private String content; // HTML-контент расписания
    private LocalDateTime createdAt; // Дата создания
    private LocalDateTime updatedAt; // Дата обновления

    // Геттер для id
    public Long getId() {
        return id;
    }

    // Сеттер для id
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для title
    public String getTitle() {
        return title;
    }

    // Сеттер для title с валидацией
    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Название расписания не может быть пустым");
        }
        this.title = title;
    }

    // Геттер для content
    public String getContent() {
        return content;
    }

    // Сеттер для content
    public void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Пожалуйста заполните контент");
        }
        this.content = content;
    }

    // Геттер для createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Геттер для updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Метод для обновления времени
    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = updatedAt;
        }
    }
}
