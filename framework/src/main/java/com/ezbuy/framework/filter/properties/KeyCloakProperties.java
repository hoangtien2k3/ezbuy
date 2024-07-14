package com.ezbuy.framework.filter.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyCloakProperties {
    private String clientId;
    private String clientSecret;
}
