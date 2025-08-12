package com.example.demo.repositories;

import com.example.demo.models.WorshipSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorshipScheduleRepository extends JpaRepository<WorshipSchedule, Long> {
}
