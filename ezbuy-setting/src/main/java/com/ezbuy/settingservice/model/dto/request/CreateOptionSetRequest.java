package com.ezbuy.settingservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOptionSetRequest {
    private String code; // ma cau hinh nhom
    private String description; // mo ta cau hinh nhom
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
}
