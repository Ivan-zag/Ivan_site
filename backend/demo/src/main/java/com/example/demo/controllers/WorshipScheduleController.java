package com.example.demo.controllers;

import com.example.demo.models.WorshipSchedule;
import com.example.demo.repositories.WorshipScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/worship-schedule")
public class WorshipScheduleController {

    @Autowired
    private WorshipScheduleRepository repository;

    // Получить все расписания (список всех строк)
    @GetMapping
    public List<WorshipSchedule> getAll() {
        return repository.findAll();
    }

    // Получить запись по id
    @GetMapping("/{id}")
    public ResponseEntity<WorshipSchedule> getById(@PathVariable Long id) {
        Optional<WorshipSchedule> schedule = repository.findById(id);
        return schedule.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Создать одну строку расписания
    @PostMapping
    public ResponseEntity<WorshipSchedule> create(@RequestBody WorshipSchedule row) {
        WorshipSchedule saved = repository.save(row);
        return ResponseEntity.ok(saved);
    }

    // Изменить существующую строку
    @PutMapping("/{id}")
    public ResponseEntity<WorshipSchedule> update(@PathVariable Long id, @RequestBody WorshipSchedule updatedRow) {
        Optional<WorshipSchedule> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        WorshipSchedule existing = optional.get();
        existing.setDay(updatedRow.getDay());
        existing.setTime(updatedRow.getTime());
        existing.setService(updatedRow.getService());
        repository.save(existing);
        return ResponseEntity.ok(existing);
    }

    // Удалить строку
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
