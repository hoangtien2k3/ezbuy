package com.ezbuy.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRegisInforDto {
    private String number; // So dang ki kinh doanh
    private String taxNo; // ma so thue
    private String taxDate; // ngay cap
    private String taxAddress; // noi cap
}
