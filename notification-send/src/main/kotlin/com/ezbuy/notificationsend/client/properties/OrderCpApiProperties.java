package com.ezbuy.notificationsend.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("orderCpApiProperties")
@ConfigurationProperties(prefix = "client.order-cp", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class OrderCpApiProperties extends WebClientProperties {
}

