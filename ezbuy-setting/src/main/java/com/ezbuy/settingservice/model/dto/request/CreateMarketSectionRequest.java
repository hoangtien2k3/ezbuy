package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateMarketSectionRequest {
    private String type; // loai section
    private String code; // ma section
    private String name; // ten section
    private String description; // mo ta
    private Long displayOrder; // thu tu hien thi
    private String data; // noi dung
    private Integer status; // trang thai
}
