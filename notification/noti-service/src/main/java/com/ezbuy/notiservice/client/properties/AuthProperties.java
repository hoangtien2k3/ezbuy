package com.ezbuy.notiservice.client.properties;

import com.ezbuy.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("authProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class AuthProperties extends WebClientProperties {
}
