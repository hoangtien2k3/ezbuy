package com.ezbuy.auth.infrastructure.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("settingClientProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
@AllArgsConstructor
public class SettingClientProperties extends WebClientProperties {
}
