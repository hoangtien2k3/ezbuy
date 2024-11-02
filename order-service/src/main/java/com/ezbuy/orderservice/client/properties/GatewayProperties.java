package com.ezbuy.orderservice.client.properties;

import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("gatewayProperties")
@ConfigurationProperties(prefix = "client.gateway", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class GatewayProperties extends WebClientProperties {}
