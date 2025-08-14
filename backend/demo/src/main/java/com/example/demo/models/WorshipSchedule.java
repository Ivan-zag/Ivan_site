package com.example.demo.models;

import jakarta.persistence.*;

@Entity
public class WorshipSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    private String time;
    private String service; // Богослужение

    // Getters and setters

    public WorshipSchedule() {
    }

    public WorshipSchedule(String day, String time, String service) {
        this.day = day;
        this.time = time;
        this.service = service;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
