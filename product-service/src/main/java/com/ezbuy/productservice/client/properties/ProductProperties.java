package com.ezbuy.productservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("productProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
@Data
@RequiredArgsConstructor
public class ProductProperties extends WebClientProperties {
    private String staffCode;
}
