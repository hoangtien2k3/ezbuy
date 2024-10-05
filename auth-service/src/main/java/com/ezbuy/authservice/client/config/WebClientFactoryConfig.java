package com.ezbuy.authservice.client.config;

import com.ezbuy.authservice.client.properties.KeycloakClientProperties;
import com.ezbuy.authservice.client.properties.NotiServiceProperties;
import io.hoangtien2k3.reactify.client.WebClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final KeycloakClientProperties keycloakClientProperties;
    private final NotiServiceProperties notiServiceProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(List.of(keycloakClientProperties, notiServiceProperties));
        return factory;
    }
}

