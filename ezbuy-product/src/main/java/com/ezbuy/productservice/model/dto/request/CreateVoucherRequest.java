package com.ezbuy.productservice.model.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherRequest {
    private String code;
    private String voucherTypeId;
    private String batchId;
    private LocalDateTime expiredDate;
    private Integer expiredPeriod;
    private String state;
    private String voucherId;
    private String organizationId;
    private String userId;
    private List<String> voucherTypeCodeList;
}
