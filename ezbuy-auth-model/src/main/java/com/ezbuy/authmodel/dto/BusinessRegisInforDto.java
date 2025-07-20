package com.ezbuy.authmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRegisInforDto {
    private String number;
    private String taxNo;
    private String taxDate;
    private String taxAddress;
}
