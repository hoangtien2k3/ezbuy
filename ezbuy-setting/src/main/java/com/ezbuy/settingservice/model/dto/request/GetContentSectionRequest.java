package com.ezbuy.settingservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetContentSectionRequest {
    private String type;
    private String refId;
    private String refType;
    private String refAlias; // alias of service
}
