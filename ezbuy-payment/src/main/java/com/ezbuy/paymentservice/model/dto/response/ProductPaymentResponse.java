package com.ezbuy.paymentservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPaymentResponse {
    private String checkoutLink;
    private String requestBankingId;
}
