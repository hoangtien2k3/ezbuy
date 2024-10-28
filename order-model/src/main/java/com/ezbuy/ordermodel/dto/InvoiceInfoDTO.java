package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceInfoDTO {
    private String fullName; // ho ten
    private String taxCode; // ma so thue
    private String phoneNumber; // so dien thoai
    private String email; // email
    private AddressDTO addressDTO; // thong tin dia chi
}
