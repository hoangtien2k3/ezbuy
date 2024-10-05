package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustIdentityDTO {

    private String custId;
    private String idNo;
    private String idType;
    private String idIssuePlace;
    private Date idIssueDate;
}
