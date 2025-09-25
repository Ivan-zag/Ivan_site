package com.example.demo;

import com.example.demo.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Доступ для всех к GET запросам на /api/news и /api/schedule/last
                        .requestMatchers(HttpMethod.GET, "/api/news", "/api/schedule/last").permitAll()
                        .requestMatchers("/api/login", "/api/public/**").permitAll()
                        // GET запросы на /api/news разрешены всем
                        .requestMatchers(HttpMethod.POST, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers(HttpMethod.PUT, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers(HttpMethod.DELETE, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers("/api/schedule/upload").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
