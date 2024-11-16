package com.ezbuy.paymentservice.client.config;

import com.ezbuy.paymentservice.client.properties.*;
import com.reactify.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthClientProperties authClientProperties;
    private final OrderClientProperties orderClientProperties;
    private final PaymentClientProperties paymentClientProperties;
    private final ProductClientProperties productClientProperties;
    private final SettingClientProperties settingClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        var listProperties = List.of(
                authClientProperties,
                orderClientProperties,
                paymentClientProperties,
                productClientProperties,
                settingClientProperties);

        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(listProperties);
        return factory;
    }
}
