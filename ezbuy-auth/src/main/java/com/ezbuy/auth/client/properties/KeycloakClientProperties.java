package com.ezbuy.auth.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("keycloakServiceProperties")
@ConfigurationProperties(prefix = "client.keycloak", ignoreInvalidFields = true)
public class KeycloakClientProperties extends WebClientProperties {

    private KeycloakAuthProperties keycloakAuth;

    @Getter
    @Setter
    public static class KeycloakAuthProperties {
        /**
         * the client ID used for authenticating with the Keycloak server
         */
        private String clientId;

        /**
         * the client secret used for authenticating with the Keycloak server
         */
        private String clientSecret;
    }
}
