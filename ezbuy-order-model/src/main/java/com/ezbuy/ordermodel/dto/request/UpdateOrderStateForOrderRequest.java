package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStateForOrderRequest {

    private String orderCode;
    private Integer paymentStatus;
}
