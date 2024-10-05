
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@Data
public class LocationDTO extends BaseDTO
{
    protected String code;
    protected String label;
    protected String name;
    protected String vtMapCode;
}
