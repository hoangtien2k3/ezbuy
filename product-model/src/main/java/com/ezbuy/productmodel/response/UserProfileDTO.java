package com.ezbuy.productmodel.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserProfileDTO {

    private String id;
    private LocalDate foundingDate;
    private String businessType;
    private String provinceCode;
    private String districtCode;
    private String image;
    private String companyName;
    private String representative;
    private String phone;
    private String taxDepartment;
    private String precinctCode;
    private String streetBlock;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String taxCode;
    private String createBy;
    private String updateBy;
    private String userId;
}
