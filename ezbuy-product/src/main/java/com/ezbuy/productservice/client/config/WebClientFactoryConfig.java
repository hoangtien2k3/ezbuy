package com.ezbuy.productservice.client.config;

import com.ezbuy.productservice.client.properties.*;
import com.ezbuy.core.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final OrderProperties orderProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                orderProperties
        ));
    }
}
