package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

@Data
public class UpdateSatePreOrderRequest {
    private String orderCode; //ma don hang
    private Integer status; //trang thai 1 - dang thuc hien, 4 huy don hang
}