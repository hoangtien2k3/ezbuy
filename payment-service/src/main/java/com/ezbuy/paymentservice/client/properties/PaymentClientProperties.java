package com.ezbuy.paymentservice.client.properties;

import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("paymentClientProperties")
@ConfigurationProperties(prefix = "client.payment", ignoreInvalidFields = true)
@Data
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
