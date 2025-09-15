package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Transactional
    public NewsDto createNews(NewsDto newsDto) {
        News news = mapToNews(newsDto);
        news.setCreatedAt(LocalDateTime.now());
        news.setActive(true);
        News savedNews = newsRepository.save(news);
        return mapToNewsDto(savedNews);
    }

    public List<NewsDto> getActiveNews() {
        List<News> activeNews = newsRepository.findByActiveTrue();
        return activeNews.stream()
                .map(this::mapToNewsDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NewsDto updateNews(Long id, NewsDto newsDto) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (!optionalNews.isPresent()) {
            throw new RuntimeException("News not found");
        }
        News news = optionalNews.get();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setImageUrl(newsDto.getImageUrl());
        news.setActive(newsDto.isActive());
        News updatedNews = newsRepository.save(news);
        return mapToNewsDto(updatedNews);
    }

    @Transactional
    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

    public void backupNews() {
        // Реализация резервного копирования
        List<News> allNews = newsRepository.findAll();
        // Здесь логика сохранения в резервное хранилище
    }

    private NewsDto mapToNewsDto(News news) {
        NewsDto dto = new NewsDto();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setImageUrl(news.getImageUrl());
        dto.setActive(news.isActive());
        dto.setCreatedAt(news.getCreatedAt());
        return dto;
    }

    private News mapToNews(NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setImageUrl(newsDto.getImageUrl());
        news.setActive(newsDto.isActive());
        return news;
    }
}
