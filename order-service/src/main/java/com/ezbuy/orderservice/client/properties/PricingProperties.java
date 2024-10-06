package com.ezbuy.orderservice.client.properties;

import io.hoangtien2k3.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("pricingProperties")
@ConfigurationProperties(prefix = "client.pricing", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PricingProperties extends WebClientProperties {

    private String systemStaffCode;
    private String staffRevenue;
}
