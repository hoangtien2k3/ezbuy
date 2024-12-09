package com.ezbuy.notificationsend.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("settingClientProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class SettingClientProperties extends WebClientProperties {
}
