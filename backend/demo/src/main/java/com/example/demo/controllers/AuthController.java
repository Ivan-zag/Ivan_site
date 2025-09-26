package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.services.*;
import com.example.demo.JWT.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService; // твой сервис

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok(Map.of("message", "Пользователь успешно создан!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

}
