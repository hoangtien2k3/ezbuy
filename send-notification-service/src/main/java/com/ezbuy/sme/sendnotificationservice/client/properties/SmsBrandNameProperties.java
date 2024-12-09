package com.ezbuy.sme.sendnotificationservice.client.properties;

import com.reactify.client.properties.WebClientProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("smsBrandNameClientProperties")
@ConfigurationProperties(prefix = "client.sms-brandname", ignoreInvalidFields = true)
@Data
@AllArgsConstructor
public class SmsBrandNameProperties extends WebClientProperties {
}
