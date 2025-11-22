package com.ezbuy.triggerservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("orderClientProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
public class OrderProperties extends WebClientProperties {
}
