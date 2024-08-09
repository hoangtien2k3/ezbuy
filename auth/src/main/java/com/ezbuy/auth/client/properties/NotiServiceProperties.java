package com.ezbuy.auth.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.client.properties.WebClientProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component("notiServiceProperties")
@ConfigurationProperties(prefix = "client.noti", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class NotiServiceProperties extends WebClientProperties {

}
