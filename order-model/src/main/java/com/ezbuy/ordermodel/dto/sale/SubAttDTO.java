
package com.ezbuy.ordermodel.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class SubAttDTO extends BaseDTO
{
    protected String attCode;
    protected String attName;
    protected String attType;
    protected String attValue;
    protected Date createDatetime;
    protected String createUser;
    protected String description;
    protected List<SubAttDetailDTO> listSubAttDetailDTO;
    protected String status;
    protected Long subAttId;
    protected Long subId;
    protected Date updateDatetime;
    protected String updateUser;
}
