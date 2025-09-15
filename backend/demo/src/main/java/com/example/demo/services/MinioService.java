package com.example.demo.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {
    private final MinioClient minioClient;
    private final String bucketName = "images"; // можно вынести в application.properties

    public MinioService() {
        this.minioClient = MinioClient.builder()
                .endpoint("http://minio:9000") // используйте внутренний docker name либо свой url
                .credentials("minioadmin", "minioadmin") // логин и пароль из minio
                .build();

        // Создать bucket если не существует
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not ensure bucket exists: " + e.getMessage());
        }
    }

    public String upload(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            try (InputStream is = file.getInputStream()) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(is, file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build());
            }
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Upload error: " + e.getMessage(), e);
        }
    }

    public void delete(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw new RuntimeException("Delete error: " + e.getMessage(), e);
        }
    }

    public InputStream get(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            throw new RuntimeException("Get error: " + e.getMessage(), e);
        }
    }

    public String getPublicUrl(String fileName) {
        // Если MinIO в публичном режиме, ссылку можно формировать так:
        return "https://back.hram48plosk.ru/images/" + fileName;
        // Для публичного доступа нужно либо на Nginx проксировать этот путь, либо
        // настроить policy в MinIO
    }
}
