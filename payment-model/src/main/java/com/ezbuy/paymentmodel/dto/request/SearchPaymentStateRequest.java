package com.ezbuy.paymentmodel.dto.request;

import lombok.Data;

@Data
public class SearchPaymentStateRequest {

    private String merchant_code;
    private String check_sum;
    private String order_code;
}
