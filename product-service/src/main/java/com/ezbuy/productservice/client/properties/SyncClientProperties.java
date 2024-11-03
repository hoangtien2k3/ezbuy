package com.ezbuy.productservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("syncClientProperties")
@ConfigurationProperties(prefix = "client.sync", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class SyncClientProperties extends WebClientProperties {}
