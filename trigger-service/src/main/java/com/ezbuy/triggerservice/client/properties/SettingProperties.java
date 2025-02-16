package com.ezbuy.triggerservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component("settingClientProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
@AllArgsConstructor
public class SettingProperties extends WebClientProperties {
}
