package com.ezbuy.productservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("caProperties")
@ConfigurationProperties(prefix = "client.ca", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class CaProperties extends WebClientProperties {
}
