package com.ezbuy.settingmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOptionSetValueRequest {
    private String code; //ma chi tiet cau hinh nhom
    private Long optionSetId; //id cau hinh nhom mapping
    private String value; //gia tri chi tiet cau hinh nhom
    private String description; //mo ta chi tiet cau hinh nhom
    private Integer status; //trang thai: 1 - hieu luc, 0 - khong hieu luc
}
