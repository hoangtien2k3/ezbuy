package com.ezbuy.productservice.client.config;

import com.ezbuy.productservice.client.properties.*;
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
    private final AuthProperties authProperties;
    private final OrderProperties orderProperties;
    private final SettingProperties settingProperties;
//    private final CaProperties caProperties;
//    private final SyncClientProperties syncClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(List.of(authProperties, orderProperties, settingProperties));
        return factory;
    }
}

