package com.ezbuy.triggerservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "cron-job")
public class JobConfigs {
    private List<JobProfile> profiles;
}
