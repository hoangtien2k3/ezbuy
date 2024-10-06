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
    private String orderCode;//ma don hang cha cua don hang combo
    private String orderCodeCombo;//ma don hang con cua don hang combo
    private String merchantCodeCombo;//ma alias cua dich vu con
    private Double amount;//so tien cua dich vu con
}
