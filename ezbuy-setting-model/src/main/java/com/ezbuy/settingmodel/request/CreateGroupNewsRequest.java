package com.ezbuy.settingmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupNewsRequest {
    private String name; // ten nhom tin tuc
    private String code; // ma nhom tin tuc
    private Integer displayOrder; // thu tu sap xep
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
}
