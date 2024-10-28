package com.ezbuy.productmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServiceGroupRequest {
    private String name; // ten nhom dich vu
    private String code; // ma nhom dich vu
    private Integer displayOrder; // thu tu hien thi
    private Integer status; // trang thai
}
