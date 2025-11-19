package com.ezbuy.triggerservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("orderClientProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
@AllArgsConstructor
public class OrderProperties extends WebClientProperties {}
