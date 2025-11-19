package com.ezbuy.cartservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("settingProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
@AllArgsConstructor
public class SettingProperties extends WebClientProperties {
}
