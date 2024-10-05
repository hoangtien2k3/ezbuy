
package com.ezbuy.ordermodel.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SubAttDetailDTO extends BaseDTO
{
    protected String attDetailCode;
    protected String attDetailName;
    protected String attDetailType;
    protected String attDetailValue;
    protected Date createDatetime;
    protected String createUser;
    protected CustomerDTO customerDTO;
    protected Long id;
    protected String status;
    protected Long subAttId;
    protected Date updateDatetime;
    protected String updateUser;
}
