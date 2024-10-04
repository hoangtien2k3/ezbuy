package com.ezbuy.notisend.client;

import com.ezbuy.notisend.client.properties.AuthProperties;
import io.hoangtien2k3.reactify.client.WebClientFactory;
import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final AuthProperties authProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        List<WebClientProperties> lstAuthProperties = List.of(authProperties);
        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(lstAuthProperties);
        return factory;
    }
}
