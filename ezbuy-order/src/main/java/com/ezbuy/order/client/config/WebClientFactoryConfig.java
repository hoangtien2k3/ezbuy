package com.ezbuy.order.client.config;

import com.ezbuy.order.client.properties.*;
import com.ezbuy.core.client.WebClientFactory;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthProperties authProperties;
    private final OrderProperties orderProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                authProperties,
                orderProperties
        ));
    }
}
