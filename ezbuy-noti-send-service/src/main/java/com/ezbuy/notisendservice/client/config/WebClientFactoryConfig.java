package com.ezbuy.notisendservice.client.config;

import com.ezbuy.notisendservice.client.properties.AuthProperties;
import com.ezbuy.core.client.WebClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthProperties authProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(authProperties));
    }
}

