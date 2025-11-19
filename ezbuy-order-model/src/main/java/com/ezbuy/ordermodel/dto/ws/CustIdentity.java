package com.ezbuy.ordermodel.dto.ws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustIdentity {

    private Long custId;
    private String idNo;
    private String idType;
    private String idIssuePlace;
    private String idIssueDate;
    private String idExpireDate;
}
