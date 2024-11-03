package com.ezbuy.productservice.client.config;

import com.ezbuy.productservice.client.properties.*;
import java.util.List;

import com.reactify.client.WebClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthProperties authProperties;
    private final OrderProperties orderProperties;
    private final SettingProperties settingProperties;
    // private final CaProperties caProperties;
    // private final SyncClientProperties syncClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        var listPropertiest = List.of(authProperties, orderProperties, settingProperties);

        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(listPropertiest);
        return factory;
    }
}
