package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.OrderDTO;
import com.ezbuy.ordermodel.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderHistoryRequest {

    private OrderDTO order;

    private List<OrderItem> orderItemList;

    private String orderType;

    private boolean fromCart;

    private String individualId;
}
