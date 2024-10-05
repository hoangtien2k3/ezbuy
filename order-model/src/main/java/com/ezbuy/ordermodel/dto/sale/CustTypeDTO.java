
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Data
public class CustTypeDTO extends BaseDTO
{
    protected Date createDatetime;
    protected String createUser;
    protected String custType;
    protected String description;
    protected String groupType;
    protected String name;
    protected String plan;
    protected String representCust;
    protected String status;
    protected Long tax;
    protected Date updateDatetime;
    protected String updateUser;
}
