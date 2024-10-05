
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;


@Data
public class GroupsExtDTO extends BaseDTO
{
    protected Date createDatetime;
    protected String createUser;
    protected String extKey;
    protected String extValue;
    protected Long groupsId;
    protected Long id;
    protected String incentivesMethodId;
    protected String status;
    protected Date updateDatetime;
    protected String updateUser;
}
