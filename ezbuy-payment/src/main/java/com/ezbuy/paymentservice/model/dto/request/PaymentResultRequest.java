package com.ezbuy.paymentservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResultRequest {
    private String error_code;
    private String check_sum;
    private Integer payment_status;
    private String order_code;
    private Long trans_amount;
    private String vt_transaction_id;
    private String merchant_code;
}
