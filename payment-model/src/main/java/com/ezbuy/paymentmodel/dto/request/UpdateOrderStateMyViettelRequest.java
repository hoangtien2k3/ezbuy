package com.ezbuy.paymentmodel.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStateMyViettelRequest {

    private String transaction_id;
    private String merchant_code;
    private String check_sum;
    private Integer status;
    private Integer payment_status;
    private String order_code;
}
