package com.ezbuy.order.client.properties;

import com.ezbuy.core.client.properties.WebClientProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("orderProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
public class OrderProperties extends WebClientProperties {
    private String systemStaffCode;
    private String staffRevenue;
}
