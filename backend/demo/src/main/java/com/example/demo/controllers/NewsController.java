package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.demo.models.*;
import com.example.demo.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    // Сервис для работы с новостями
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping
    public ResponseEntity<NewsDto> createNews(@RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile imageUrl) {
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(content);

        // Логика сохранения изображения, если оно загружено
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Сохраните изображение и установите путь к нему в newsDto
            String imagePath = saveImage(imageUrl);
            newsDto.setImageUrl(imagePath);
        }

        NewsDto createdNews = newsService.createNews(newsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    private String saveImage(MultipartFile image) {
        // Каталог, в который будут сохранены изображения
        String uploadDir = "/app/uploads"; // Каталог
        // Убедитесь, что директория существует, и создайте её, если необходимо
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Генерация уникального имени файла
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

        // Путь для сохранения файла
        File destinationFile = new File(uploadDirectory, fileName);

        try {
            // Сохраняем файл
            image.transferTo(destinationFile);
            System.out.println("Файл сохранён: " + destinationFile.getPath());
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Не удалось сохранить файл", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> getActiveNews() {
        List<NewsDto> activeNews = newsService.getActiveNews();
        return ResponseEntity.ok(activeNews);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> updateNews(@PathVariable Long id, @RequestBody NewsDto newsDto) {
        NewsDto updatedNews = newsService.updateNews(id, newsDto);
        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @Scheduled(cron = "0 0 0 1 */2 *") // Резервное копирование каждые 2 месяца
    public void backupNews() {
        newsService.backupNews();
    }
}
