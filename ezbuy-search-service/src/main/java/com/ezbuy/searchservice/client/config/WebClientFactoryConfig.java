package com.ezbuy.searchservice.client.config;

import com.ezbuy.searchservice.client.properties.ElasticsearchClientProperties;
import com.ezbuy.core.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final ElasticsearchClientProperties elasticsearchClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(elasticsearchClientProperties));
    }
}
