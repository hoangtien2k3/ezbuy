package com.ezbuy.triggerservice.client.config;

import com.ezbuy.triggerservice.client.properties.OrderProperties;
import com.ezbuy.triggerservice.client.properties.ProductProperties;
import com.ezbuy.triggerservice.client.properties.SettingProperties;
import com.reactify.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final ProductProperties productProperties;
    private final OrderProperties orderProperties;
    private final SettingProperties settingProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(productProperties, orderProperties, settingProperties));
    }
}
