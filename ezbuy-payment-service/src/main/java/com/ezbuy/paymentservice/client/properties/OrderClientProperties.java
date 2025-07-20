package com.ezbuy.paymentservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("orderClientProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
@AllArgsConstructor
public class OrderClientProperties extends WebClientProperties {}
