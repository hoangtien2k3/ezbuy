package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private String areaCode;

    private String province;

    private String provinceName;

    private String district;

    private String districtName;

    private String precinct;

    private String precinctName;

    private String fullName;

    private String address; // dia chi chi tiet
}
