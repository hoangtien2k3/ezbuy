package com.ezbuy.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String baseUrl;
    private String publicUrl;
    private String accessKey;
    private String secretKey;
    private String bucket;
}
