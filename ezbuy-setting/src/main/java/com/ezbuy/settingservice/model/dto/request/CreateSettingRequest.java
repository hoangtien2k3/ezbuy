package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSettingRequest {
    private String code; // ma code
    private String value; // gia tri
    private String description; // chi tiet
    private Integer status; // trang thai
}
