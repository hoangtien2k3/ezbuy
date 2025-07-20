package com.ezbuy.triggerservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("productClientProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
@AllArgsConstructor
public class ProductProperties extends WebClientProperties {}
