package com.ezbuy.authservice.client.properties;

import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("notiServiceProperties")
@ConfigurationProperties(prefix = "client.noti", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class NotiServiceProperties extends WebClientProperties {}
