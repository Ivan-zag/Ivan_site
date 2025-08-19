package com.example.demo.services;
import com.example.demo.models.WorshipSchedule;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorshipScheduleService {

    @Autowired
    private WorshipScheduleRepository repository;

    public WorshipSchedule saveSchedule(String title, String htmlContent) {
        WorshipSchedule schedule = new WorshipSchedule();
        schedule.setTitle(title);
        schedule.setContent(htmlContent);
        schedule.setCreatedAt(LocalDateTime.now());
        schedule.setUpdatedAt(LocalDateTime.now());
        return repository.save(schedule);
    }

    public Optional<WorshipSchedule> getScheduleById(Long id) {
        return repository.findById(id);
    }

    public Optional<WorshipSchedule> getLastSchedule() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().findFirst();
    }

    public Optional<WorshipSchedule> updateSchedule(Long id, String newContent) {
        Optional<WorshipSchedule> optional = repository.findById(id);
        if (optional.isPresent()) {
            WorshipSchedule schedule = optional.get();
            schedule.setContent(newContent);
            schedule.setUpdatedAt(LocalDateTime.now());
            repository.save(schedule);
            return Optional.of(schedule);
        }
        return Optional.empty();
    }

    public void deleteSchedule(Long id) {
        repository.deleteById(id);
    }
}
