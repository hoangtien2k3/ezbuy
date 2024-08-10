package com.ezbuy.authmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PresentativeDto {
    private String presentativeId;
    private String tenantId;
    private String name;
    private String positionsId;
    private String positionCode;
    private String idType;
    private String idNo;
    private String expireDate;
    private String dateOfBirth;
    private String gender;
    private String dateOfIssue;
    private String placeOfIssue;
    private String email;
    private String provinceCode;
    private String districtCode;
    private String precinctCode;
    private String address;
    private String phone;
}
