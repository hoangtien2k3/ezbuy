package com.ezbuy.paymentmodel.dto.response;

import lombok.Data;

@Data
public class SearchPaymentState {

    private Integer payment_status;
    private String vt_transaction_id;
    private Long trans_amount;
    private Long viettelid_point;
    private Long money_amount;
    private String voucher_code;
    private String merchant_code;
    private String order_code;
    private String check_sum;
}
