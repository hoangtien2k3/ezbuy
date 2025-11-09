package com.ezbuy.orderservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("cmProperties")
@ConfigurationProperties(prefix = "client.cm", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class CmProperties extends WebClientProperties {}
