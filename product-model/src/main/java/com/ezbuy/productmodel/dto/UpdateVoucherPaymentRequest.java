package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateVoucherPaymentRequest {
    private String sourceOrderId;
    private String voucherTransState;
    private String voucherUseState;
    private Boolean isOrderHistory;
}
