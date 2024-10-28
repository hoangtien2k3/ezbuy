package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientInfoDTO {
    private String name; // ten nguoi lien he
    private String idNo; // so giay to
    private String idType; // loai giay to
    private String position; // chuc vu
    private String issueBy; // noi cap
    private String issueDate; // ngay cap
    private String phone; // so dien thoai
    private String email; // email
    private String contactTitle; // cung la chuc vu
    private AddressDTO addressDTO; // thong tin dia chi
}
