package com.ezbuy.paymentservice.client.properties;

import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("authClientProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class AuthClientProperties extends WebClientProperties {
}
