package com.ezbuy.cartservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("productProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
public class ProductProperties extends WebClientProperties {
}
