package com.ezbuy.auth.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.client.properties.WebClientProperties;
import com.ezbuy.framework.filter.properties.KeyCloakProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component("keycloakProperties")
@ConfigurationProperties(prefix = "client.keycloak", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakClientProperties extends WebClientProperties {
    private KeyCloakProperties auth;
}
