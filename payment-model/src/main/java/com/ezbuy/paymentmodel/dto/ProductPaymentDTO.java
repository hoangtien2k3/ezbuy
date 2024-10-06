package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPaymentDTO {

    private String templateId;
    private Integer quantity;
    private String telecomServiceId;
}
