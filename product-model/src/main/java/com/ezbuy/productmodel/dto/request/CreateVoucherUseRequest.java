package com.ezbuy.productmodel.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherUseRequest {
    private String voucherId;
    private String userId;
    private String usename;
    private String systemType;
    private LocalDateTime expiredDate;
    private String state;
    private String sourceOrderId;
}
