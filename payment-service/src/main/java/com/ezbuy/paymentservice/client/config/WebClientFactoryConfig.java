package com.ezbuy.paymentservice.client.config;

import com.ezbuy.paymentservice.client.properties.*;
import com.reactify.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthClientProperties authClientProperties;
    private final OrderClientProperties orderClientProperties;
    private final PaymentClientProperties paymentClientProperties;
    private final ProductClientProperties productClientProperties;
    private final SettingClientProperties settingClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                authClientProperties,
                orderClientProperties,
                paymentClientProperties,
                productClientProperties,
                settingClientProperties
            )
        );
    }
}
