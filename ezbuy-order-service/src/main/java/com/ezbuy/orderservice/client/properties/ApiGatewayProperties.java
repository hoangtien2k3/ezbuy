package com.ezbuy.orderservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("gatewayClientProperties")
@ConfigurationProperties(prefix = "client.api-gateway", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class ApiGatewayProperties extends WebClientProperties {}
