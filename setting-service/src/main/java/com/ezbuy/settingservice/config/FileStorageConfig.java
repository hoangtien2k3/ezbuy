package com.ezbuy.settingservice.config;

import org.springframework.context.annotation.Configuration;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configuration
public class FileStorageConfig {
    @Value("${minio.baseUrl}")
    private String minioBaseUrl;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(minioBaseUrl).credentials(minioAccessKey, minioSecretKey).build();
    }
}
