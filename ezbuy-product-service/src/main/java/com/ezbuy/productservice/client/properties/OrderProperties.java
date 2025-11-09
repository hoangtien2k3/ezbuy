package com.ezbuy.productservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("orderProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class OrderProperties extends WebClientProperties {}
