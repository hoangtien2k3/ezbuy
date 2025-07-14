package com.ezbuy.paymentmodel.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
