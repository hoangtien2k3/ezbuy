package com.ezbuy.cartservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("paymentProperties")
@ConfigurationProperties(prefix = "client.payment", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class PaymentProperties extends WebClientProperties {}
