package com.ezbuy.ordermodel.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
