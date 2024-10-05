package com.ezbuy.orderservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("productProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class ProductProperties extends WebClientProperties {
}
