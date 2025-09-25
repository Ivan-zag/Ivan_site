package com.example.demo.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 3, max = 20, message = "Имя пользователя должно быть длиной от 3 до 20 символов")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 6, message = "Пароль должен быть длиной не менее 6 символов")
    private String password;

    @Email(message = "Неверный формат email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @NotBlank(message = "Роль не может быть пустой")
    private String role;

    // Конструкторы, геттеры и сеттеры

    public UserDto() {
    }

    public UserDto(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Метод для конвертации строки роли в enum Role
    public Role getRoleAsEnum() {
        try {
            return Role.valueOf(role.toUpperCase()); // Обеспечивает соответствие строк и значений enum
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Некорректная роль: " + role);
        }
    }
}
