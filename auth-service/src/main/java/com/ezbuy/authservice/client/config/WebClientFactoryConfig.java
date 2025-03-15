package com.ezbuy.authservice.client.config;

import com.ezbuy.authservice.client.properties.KeycloakClientProperties;
import com.ezbuy.authservice.client.properties.NotiServiceProperties;
import com.ezbuy.authservice.client.properties.SettingClientProperties;
import com.reactify.client.WebClientFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebClientFactoryConfig {
    private final KeycloakClientProperties keycloakClientProperties;
    private final NotiServiceProperties notiServiceProperties;
    private final SettingClientProperties settingClientProperties;

    @Bean(name = "webClientFactory")
    public WebClientFactory webClientFactory() {
        return new WebClientFactory(List.of(
                keycloakClientProperties,
                notiServiceProperties,
                settingClientProperties
        ));
    }
}
