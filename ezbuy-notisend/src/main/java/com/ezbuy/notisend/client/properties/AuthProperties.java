package com.ezbuy.notisend.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("authProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
public class AuthProperties extends WebClientProperties {
}
