package com.ezbuy.auth.infrastructure.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Getter
@NoArgsConstructor
@Configuration
public class KeycloakProvider {

    @Value("${keycloak.server-url}")
    private String serverURL;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientID;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.host}")
    private String hostKeycloak;

    private volatile Keycloak keycloak;

    //Double-Checked Locking Keycloak
    public Keycloak getInstance() {
        if (keycloak == null) {
            synchronized (KeycloakProvider.class) {
                if (keycloak == null) {
                    keycloak = KeycloakBuilder.builder()
                            .serverUrl(serverURL)
                            .realm(realm)
                            .clientId(clientID)
                            .clientSecret(clientSecret)
                            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                            .build();
                }
            }
        }
        return keycloak;
    }

    public RealmResource getRealmResource() {
        return getInstance().realm(realm);
    }

    public Mono<ClientRepresentation> getClient(String clientId) {
        var clients = getRealmResource().clients().findByClientId(clientId);
        if (clients == null) {
            return Mono.empty();
        }
        for (ClientRepresentation clientRepresentation : clients) {
            if (clientRepresentation.isEnabled()) {
                return Mono.justOrEmpty(clientRepresentation);
            }
        }
        return Mono.empty();
    }

    public Mono<String> getKCIdFromClientId(String clientId) {
        return getClient(clientId).flatMap(rs -> Mono.just(rs.getId()));
    }

    public Mono<ClientRepresentation> getClientWithSecret(String clientId) {
        return getClient(clientId).flatMap(clientRepresentation -> {
            var result = getRealmResource()
                    .clients()
                    .get(clientRepresentation.getId())
                    .getSecret();
            clientRepresentation.setSecret(result.getValue());
            return Mono.just(clientRepresentation);
        });
    }
}
