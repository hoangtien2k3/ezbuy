package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

@Data
public class LocationInfoDTO extends BaseDTO {
    protected String address;
    protected String areaCode;
    protected String district;
    protected String districtName;
    protected String home;
    protected String precinct;
    protected String precinctName;
    protected String province;
    protected String provinceName;
    protected String streetBlock;
    protected String streetBlockName;
    protected String streetName;
}
