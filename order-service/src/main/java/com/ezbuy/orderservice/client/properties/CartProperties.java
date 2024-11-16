package com.ezbuy.orderservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("cartProperties")
@ConfigurationProperties(prefix = "client.cart", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class CartProperties extends WebClientProperties {}
