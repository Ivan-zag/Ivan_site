package com.example.demo.controllers;

import com.example.demo.models.WorshipSchedule;
import com.example.demo.services.WorshipScheduleService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private WorshipScheduleService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadSchedule(@RequestParam("file") MultipartFile file,
            @RequestParam("title") String title) {
        try {
            InputStream docStream = new ByteArrayInputStream(file.getBytes());
            XWPFDocument doc = new XWPFDocument(docStream);
            // Здесь должна быть ваша логика конвертации doc -> HTML
            String htmlContent = "..."; // TODO: реализуйте
            WorshipSchedule schedule = service.saveSchedule(title, htmlContent);
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка загрузки Word-файла");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id) {
        Optional<WorshipSchedule> schedule = service.getScheduleById(id);
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSchedule(@PathVariable Long id, @RequestBody WorshipSchedule updated) {
        Optional<WorshipSchedule> schedule = service.updateSchedule(id, updated.getContent());
        return schedule.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        service.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}
