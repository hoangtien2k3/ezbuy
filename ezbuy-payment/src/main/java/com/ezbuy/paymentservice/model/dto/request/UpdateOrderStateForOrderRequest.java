package com.ezbuy.paymentservice.model.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStateForOrderRequest {
    private String orderCode;
    private Integer paymentStatus;
}
