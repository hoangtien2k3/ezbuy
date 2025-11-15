package com.ezbuy.auth.infrastructure.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@EqualsAndHashCode(callSuper = true)
@Component("notiServiceProperties")
@ConfigurationProperties(prefix = "client.notification", ignoreInvalidFields = true)
public class NotiServiceProperties extends WebClientProperties {
}
