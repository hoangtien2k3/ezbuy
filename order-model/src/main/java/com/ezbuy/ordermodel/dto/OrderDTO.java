package com.ezbuy.ordermodel.dto;

import com.ezbuy.ordermodel.model.Order;
import lombok.Data;

@Data
public class OrderDTO extends Order {
    private String systemType;
    private String extCode; // truong orderId cua HUB truyen sang order luc tao don hang
}
