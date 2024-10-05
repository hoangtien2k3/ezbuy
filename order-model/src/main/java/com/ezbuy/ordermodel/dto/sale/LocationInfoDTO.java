
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@Data
public class LocationInfoDTO extends BaseDTO
{
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
