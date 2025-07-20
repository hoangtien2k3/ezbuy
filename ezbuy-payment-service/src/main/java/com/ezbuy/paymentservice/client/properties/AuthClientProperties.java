package com.ezbuy.paymentservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("authClientProperties")
@ConfigurationProperties(prefix = "client.auth", ignoreInvalidFields = true)
@AllArgsConstructor
public class AuthClientProperties extends WebClientProperties {}
