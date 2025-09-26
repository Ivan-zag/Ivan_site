package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Аутентификация по логину и паролю.
     */
    @Transactional(readOnly = true)
    public Optional<User> authenticate(@NonNull String username, @NonNull String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    /**
     * Получить пользователя по логину (username).
     */
    @Transactional(readOnly = true)
    public Optional<User> getByLogin(@NonNull String login) {
        return userRepository.findByUsername(login);
    }

    /**
     * Генерация access-токена для пользователя.
     */
    // public String generateToken(@NonNull User user) {
    // return jwtService.generateToken(user);
    // Обычно в generateToken лучше передавать весь User,
    // чтобы в JwtService можно было добавить роли и т.д. в токен.
    // }

    /**
     * Регистрация пользователя.
     */
    @Transactional
    public void registerUser(@NonNull UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует!");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(userDto.getRoleAsEnum());
        userRepository.save(user);
    }
}
