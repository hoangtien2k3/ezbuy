package com.ezbuy.authservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import com.reactify.filter.properties.KeyCloakProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("keycloakProperties")
@ConfigurationProperties(prefix = "client.keycloak", ignoreInvalidFields = true)
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakClientProperties extends WebClientProperties {
    private KeyCloakProperties auth;
}
