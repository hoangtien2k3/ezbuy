package com.ezbuy.cartservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("productProperties")
@ConfigurationProperties(prefix = "client.product", ignoreInvalidFields = true)
@AllArgsConstructor
public class ProductProperties extends WebClientProperties {}
