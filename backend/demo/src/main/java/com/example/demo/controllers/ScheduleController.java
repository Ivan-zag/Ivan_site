package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadSchedule(
        @RequestParam("file") MultipartFile file  // Принимаем Word-файл
    ) {
        // Логика конвертации в HTML через Apache POI
        // Сохранение в БД
        return ResponseEntity.ok("File uploaded and processed");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getSchedule(
        @PathVariable String id  // Динамический ID из URL
    ) {
        // Получение HTML из БД по ID
        String htmlContent = "<html>Расписание</html>";
        return ResponseEntity.ok()
            .header("Content-Type", "text/html")
            .body(htmlContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchedule(
        @PathVariable String id,
        @RequestBody String htmlContent  // Новое HTML-содержимое
    ) {
        // Обновление контента в БД
        return ResponseEntity.ok("Schedule updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(
        @PathVariable String id
    ) {
        // Удаление записи из БД
        return ResponseEntity.ok("Schedule deleted");
    }
}
