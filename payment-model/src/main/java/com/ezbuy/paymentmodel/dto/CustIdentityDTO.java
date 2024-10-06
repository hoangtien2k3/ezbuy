package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class CustIdentityDTO {

    private Long custId;
    private String idNo;
    private String idType;
    private String idIssuePlace;
    private String idIssueDate;
}
