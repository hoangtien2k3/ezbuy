package com.viettel.sme.cartservice.client.config;

import com.viettel.sme.cartservice.client.properties.PaymentProperties;
import com.viettel.sme.cartservice.client.properties.ProductProperties;
import com.viettel.sme.cartservice.client.properties.SettingProperties;
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
    private final PaymentProperties paymentProperties;
    private final ProductProperties productProperties;
    private final SettingProperties settingProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory(
            ApplicationContext applicationContext, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        WebClientFactory factory = new WebClientFactory(applicationContext, authorizedClientManager);
        factory.setWebClients(List.of(paymentProperties, productProperties, settingProperties));
        return factory;
    }
}

