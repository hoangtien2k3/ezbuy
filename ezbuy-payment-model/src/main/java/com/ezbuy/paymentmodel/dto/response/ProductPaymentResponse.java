package com.ezbuy.paymentmodel.dto.response;

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
