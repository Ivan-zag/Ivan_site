package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/message")
public class TestController {
    @GetMapping("/hello")
    public ResponseEntity <String> get()
    {
        return ResponseEntity.ok("hello");
    }
}
