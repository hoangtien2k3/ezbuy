package com.ezbuy.orderservice.client.properties;

import com.ezbuy.sme.framework.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("orderProperties")
@ConfigurationProperties(prefix = "client.order", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProperties extends WebClientProperties {

    private String systemStaffCode;
    private String staffRevenue;
}
