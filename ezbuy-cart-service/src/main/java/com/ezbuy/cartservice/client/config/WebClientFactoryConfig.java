package com.ezbuy.cartservice.client.config;

import com.ezbuy.cartservice.client.properties.PaymentProperties;
import com.ezbuy.cartservice.client.properties.ProductProperties;
import com.ezbuy.cartservice.client.properties.SettingProperties;
import com.ezbuy.core.client.WebClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final PaymentProperties paymentProperties;
    private final ProductProperties productProperties;
    private final SettingProperties settingProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                paymentProperties,
                productProperties,
                settingProperties
        ));
    }
}
