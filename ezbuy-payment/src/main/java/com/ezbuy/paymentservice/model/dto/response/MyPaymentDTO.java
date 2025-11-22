package com.ezbuy.paymentservice.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPaymentDTO {
    private Integer errorCode;
    private String message;
    private SearchPaymentState data;
}
