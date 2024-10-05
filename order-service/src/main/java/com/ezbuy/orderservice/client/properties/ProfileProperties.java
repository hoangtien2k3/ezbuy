package com.ezbuy.orderservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("profileProperties")
@ConfigurationProperties(prefix = "client.profile", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class ProfileProperties extends WebClientProperties {
}
