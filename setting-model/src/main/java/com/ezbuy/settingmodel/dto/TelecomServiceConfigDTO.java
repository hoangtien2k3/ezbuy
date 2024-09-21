package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelecomServiceConfigDTO {
    private String telecomServiceId;
    private String jsonConfig;
    private String name;
    private String clientId;
    private String originalId;
    private String alias;
}
