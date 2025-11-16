package com.ezbuy.auth.infrastructure.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("notiServiceProperties")
@ConfigurationProperties(prefix = "client.notification", ignoreInvalidFields = true)
public class NotiServiceProperties extends WebClientProperties {
}
