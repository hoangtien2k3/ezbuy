
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
public class FullAddressDTO extends BaseDTO
{
    protected String address2FirstLevel;
    protected String areaCode;
    protected LocationDTO district;
    protected String fullAddress;
    protected boolean isEnteredManually;
    protected String number;
    protected LocationDTO precinct;
    protected LocationDTO province;
    protected String street;
    protected LocationDTO streetBlock;
}
