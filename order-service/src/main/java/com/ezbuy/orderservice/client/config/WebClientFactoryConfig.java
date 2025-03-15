package com.ezbuy.orderservice.client.config;

import com.ezbuy.orderservice.client.properties.*;
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
    private final AuthProperties authProperties;
    private final CartProperties cartProperties;
    private final CmPortalProperties cmPortalProperties;
    private final CmProperties cmProperties;
    private final GatewayProperties gatewayProperties;
    private final OrderProperties orderProperties;
    private final OrderV2Properties orderV2Properties;
    private final PaymentProperties properties;
    private final PricingProperties pricingProperties;
    private final ProfileProperties profileProperties;
    private final SettingProperties settingClientProperties;
    private final ProductProperties productProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                authProperties,
                cartProperties,
                cmPortalProperties,
                cmProperties,
                gatewayProperties,
                orderProperties,
                orderV2Properties,
                properties,
                pricingProperties,
                profileProperties,
                settingClientProperties,
                productProperties
            )
        );
    }
}
