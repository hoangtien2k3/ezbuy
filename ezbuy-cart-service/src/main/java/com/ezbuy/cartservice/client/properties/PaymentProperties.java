package com.ezbuy.cartservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("paymentProperties")
@ConfigurationProperties(prefix = "client.payment", ignoreInvalidFields = true)
@AllArgsConstructor
public class PaymentProperties extends WebClientProperties {}
