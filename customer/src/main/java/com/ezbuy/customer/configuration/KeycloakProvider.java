package com.ezbuy.customer.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Configuration
public class KeycloakProvider {
    @Value("${keycloak.serverUrl}")
    public String serverURL;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.clientId}")
    public String clientID;

    @Value("${keycloak.clientSecret}")
    public String clientSecret;

    private static Keycloak keycloak = null;

    public Keycloak getInstance() {
        if (keycloak != null) {
            return keycloak;
        }
        return KeycloakBuilder.builder()
                .realm(realm)
                .serverUrl(serverURL)
                .clientId(clientID)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
