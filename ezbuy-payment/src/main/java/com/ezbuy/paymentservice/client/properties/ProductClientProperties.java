package com.ezbuy.paymentservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("productClientProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
public class ProductClientProperties extends WebClientProperties {
}
