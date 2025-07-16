package com.ezbuy.paymentservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("paymentClientProperties")
@ConfigurationProperties(prefix = "client.payment", ignoreInvalidFields = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentClientProperties extends WebClientProperties {
    private String merchantCode;
    private String transType;
    private String accessCode;
    private String hashCode;
    private String templateCheckoutComboLink;
    private String templateCheckoutLink;
    private String algorithm;
}
