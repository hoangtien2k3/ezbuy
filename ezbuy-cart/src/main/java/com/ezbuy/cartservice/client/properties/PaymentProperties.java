package com.ezbuy.cartservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("paymentProperties")
@ConfigurationProperties(prefix = "client.payment", ignoreInvalidFields = true)
public class PaymentProperties extends WebClientProperties {
}
