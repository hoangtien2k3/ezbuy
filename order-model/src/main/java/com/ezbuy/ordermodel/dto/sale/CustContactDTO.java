
package com.ezbuy.ordermodel.dto.sale;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Data
public class CustContactDTO extends BaseDTO
{
    protected String billAddress;
    protected String contactLevel;
    protected String contactName;
    protected String contactTittle;
    protected String contactType;
    protected String contents;
    protected Date createDatetime;
    protected String createUser;
    protected Long custContactId;
    protected Long custId;
    protected CustomerDTO customerDTO;
    protected String email;
    protected String errorEmailMsg;
    protected String errorTelephoneNumberMsg;
    protected String status;
    protected String telephone;
    protected Date updateDatetime;
    protected String updateUser;
}
