package com.ezbuy.productservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("cmProperties")
@ConfigurationProperties(prefix = "client.cm", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class CmProperties extends WebClientProperties {
}