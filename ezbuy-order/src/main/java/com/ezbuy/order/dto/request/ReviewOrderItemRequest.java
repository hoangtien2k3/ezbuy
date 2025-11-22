package com.ezbuy.order.dto.request;

import lombok.Data;

@Data
public class ReviewOrderItemRequest {
    private String orderItemId;
    private String content;
}
