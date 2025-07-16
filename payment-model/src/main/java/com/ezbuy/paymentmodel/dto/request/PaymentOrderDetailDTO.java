package com.ezbuy.paymentmodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentOrderDetailDTO {
    private String orderCode;
    private String orderCodeCombo;
    private String merchantCodeCombo;
    private Double amount;
}
