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
    private String sourceOrderId; // Id don hang hub
    private String voucherTransState; // trang thai voucher transaction
    private String voucherUseState; // trang thai voucher use
    private Boolean isOrderHistory; // luong cap nhat lich su don hang
}
