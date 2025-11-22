package com.ezbuy.settingservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelecomClient {
    private String clientId;
    private String adminRole;
    private String serviceAlias; // serviceAlias cua dich vu
    private String alias;
}
