package com.example.demo.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class WorshipSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob // Для содержания большого HTML
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public WorshipSchedule() {
        // Для JPA обязательно нужен конструктор без параметров
    }

    // геттеры и сеттеры — ваши, можно оставить с валидацией
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Название расписания не может быть пустым");
        }
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("Пожалуйста заполните контент");
        }
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Метод для обновления времени (можно убрать — рекомендуется использовать
    // @PrePersist и @PreUpdate)
    public void updateTimestamps() {
        this.updatedAt = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = updatedAt;
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
