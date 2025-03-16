package com.ezbuy.productservice.client.config;

import com.ezbuy.productservice.client.properties.*;
import com.reactify.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final AuthProperties authProperties;
    private final OrderProperties orderProperties;
    private final SettingProperties settingProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(authProperties, orderProperties, settingProperties));
    }
}
