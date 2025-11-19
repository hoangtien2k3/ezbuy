package com.ezbuy.auth.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component("settingClientProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
public class SettingClientProperties extends WebClientProperties {
}
