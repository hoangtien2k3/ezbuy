package com.ezbuy.paymentservice.client.properties;

import com.viettel.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("productClientProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class ProductClientProperties extends WebClientProperties {
}
