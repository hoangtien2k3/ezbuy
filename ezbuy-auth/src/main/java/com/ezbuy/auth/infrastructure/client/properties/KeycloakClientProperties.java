package com.ezbuy.auth.infrastructure.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import com.ezbuy.core.filter.properties.KeyCloakProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(callSuper = true)
@Component("keycloakProperties")
@ConfigurationProperties(prefix = "client.keycloak", ignoreInvalidFields = true)
public class KeycloakClientProperties extends WebClientProperties {
    private KeyCloakProperties auth;
}
