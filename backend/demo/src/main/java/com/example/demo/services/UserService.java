package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Здесь замени на свою логику поиска по базе!
    @Transactional
    public User authenticate(String username, String password) {
        // Например, найти в базе
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // В реальном проекте — сравнивай через BCrypt, не храни пароли в открытом виде!
            return user;
        }
        return null;
    }

    public String generateToken(User user) {
        // Сюда вставь логику генерации JWT (или просто "заглушка" для теста)
        return "dummy-jwt-token-for-" + user.getUsername();
    }
}
