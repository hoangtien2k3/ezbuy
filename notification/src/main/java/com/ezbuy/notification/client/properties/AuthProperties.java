package com.ezbuy.notification.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ezbuy.framework.client.properties.WebClientProperties;

import lombok.AllArgsConstructor;
import lombok.Data;

@Component("authProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class AuthProperties extends WebClientProperties {

}
