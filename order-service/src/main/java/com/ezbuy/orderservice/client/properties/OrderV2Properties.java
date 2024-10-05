package com.ezbuy.orderservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("orderV2Properties")
@ConfigurationProperties(prefix = "client.order-v2", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class OrderV2Properties extends WebClientProperties {
}
