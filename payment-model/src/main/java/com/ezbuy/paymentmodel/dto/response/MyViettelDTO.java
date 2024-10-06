package com.ezbuy.paymentmodel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyViettelDTO {

    private Integer errorCode;
    private String message;
    private SearchPaymentState data;
}
