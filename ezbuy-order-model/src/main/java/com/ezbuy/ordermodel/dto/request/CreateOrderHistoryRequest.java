package com.ezbuy.ordermodel.dto.request;

import com.ezbuy.ordermodel.dto.OrderDTO;
import com.ezbuy.ordermodel.model.OrderItem;
import java.util.List;
import lombok.Data;

@Data
public class CreateOrderHistoryRequest {

    private OrderDTO order;

    private List<OrderItem> orderItemList;

    private String orderType;

    private boolean fromCart;

    private String individualId;
}
