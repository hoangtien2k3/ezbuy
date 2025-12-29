package com.ezbuy.auth.config;

import com.ezbuy.core.config.properties.KeycloakProperties;
import lombok.Getter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Getter
@Configuration
public class KeycloakProvider {

    private final KeycloakProperties keycloakProperties;
    private volatile Keycloak keycloak;

    public KeycloakProvider(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    //Double-Checked Locking Keycloak
    public Keycloak getInstance() {
        if (keycloak == null) {
            synchronized (KeycloakProvider.class) {
                if (keycloak == null) {
                    keycloak = KeycloakBuilder.builder()
                            .serverUrl(keycloakProperties.getServerUrl())
                            .realm(keycloakProperties.getRealm())
                            .clientId(keycloakProperties.getClientId())
                            .clientSecret(keycloakProperties.getClientSecret())
                            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                            .build();
                }
            }
        }
        return keycloak;
    }

    public RealmResource getRealmResource() {
        return getInstance().realm(keycloakProperties.getRealm());
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
