package com.ezbuy.settingservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {
    private String areaCode;
    private String province;
    private String dictrict;
    private String precinct;
    private String name;
    private String provinceName;
    private String districtName;
    private String precinctName;
    private Integer status;
}
