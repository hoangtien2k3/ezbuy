package com.ezbuy.cartservice.client.config;

import com.ezbuy.cartservice.client.properties.PaymentProperties;
import com.ezbuy.cartservice.client.properties.ProductProperties;
import com.ezbuy.cartservice.client.properties.SettingProperties;
import com.reactify.client.WebClientFactory;
import com.reactify.client.properties.WebClientProperties;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;

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
        List<WebClientProperties> lstWebClientProperties =
                List.of(paymentProperties, productProperties, settingProperties);
        factory.setWebClients(lstWebClientProperties);
        return factory;
    }
}
