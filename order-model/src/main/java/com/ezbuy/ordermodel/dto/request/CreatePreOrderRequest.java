package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.OrderProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreatePreOrderRequest {

    private Map<String, Object> customerInfo;

    private List<OrderProductDTO> productList;

    private String orderType;

    private String organizationId;//ID khach hang chon luc dang nhap

    private boolean fromCart;

    @JsonIgnore
    private boolean getAdvice;
}
