package com.example.demo;

import com.example.demo.JWT.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Доступ для всех к GET запросам на /api/news и /api/schedule/last
                        .requestMatchers(HttpMethod.GET, "/api/news", "/api/schedule/last").permitAll()
                        .requestMatchers("/api/login", "/api/register", "/api/public/**").permitAll()
                        // GET запросы на /api/news разрешены всем
                        .requestMatchers(HttpMethod.POST, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers(HttpMethod.PUT, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers(HttpMethod.DELETE, "/api/news").hasRole("ADMIN") // Только админы
                        .requestMatchers("/api/schedule/upload").hasRole("ADMIN")
                        .anyRequest().authenticated()
                        .and()
                        .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class))
                .build();
        return http.build();
    }
}
