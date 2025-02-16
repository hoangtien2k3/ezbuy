package com.ezbuy.triggerservice.client.config;

import com.ezbuy.triggerservice.client.properties.OrderProperties;
import com.ezbuy.triggerservice.client.properties.ProductProperties;
import com.ezbuy.triggerservice.client.properties.SettingProperties;
import com.reactify.client.WebClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final ProductProperties productProperties;
    private final OrderProperties orderProperties;
    private final SettingProperties settingProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        var listProperties = List.of(productProperties, orderProperties, settingProperties);
        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(listProperties);
        return factory;
    }
}
