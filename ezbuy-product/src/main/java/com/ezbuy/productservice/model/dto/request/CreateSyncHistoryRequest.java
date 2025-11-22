package com.ezbuy.productservice.model.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSyncHistoryRequest {
    private String orgId;
    private String idNo;
    private String action;
    private String serviceType;
    private String dstService;
    private String syncType;
    private String objectType;
    private String requestId;
    private String responseData;
    private LocalDateTime responseAt;
    private String errorCode;
    private String responseMessage;
    private Integer retry;
    private String state;
    private Integer status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String createBy;
    private String updateBy;
    private String ids;
}
