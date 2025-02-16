package com.ezbuy.triggerservice.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cron-job")
public class JobConfigs {
    private List<JobProfile> profiles;
}
