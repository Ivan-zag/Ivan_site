package com.example.demo.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public FileStorageService() throws IOException {
        // Создать директорию uploads при первом запуске
        if (Files.notExists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
    }

    public String store(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path destination = rootLocation.resolve(filename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
