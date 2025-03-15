package com.ezbuy.authmodel.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantIdentifyDTO {
    private String id;
    private String idType;
    private String idNo;
    private String taxDepartment;
    private LocalDateTime issueDate;
    private String issuedBy;
    private LocalDateTime expirationDate;
    private String note;
    private Integer primaryIdentify;
    private Integer trustStatus;
    private String type;
    private String tenantId;
    private Integer status;
}
