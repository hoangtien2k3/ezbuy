package com.ezbuy.productservice.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("settingProperties")
@ConfigurationProperties(prefix = "client.setting", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class SettingProperties extends WebClientProperties {}
