package com.ezbuy.order.dto.request;

import lombok.Data;

@Data
public class SearchOrderRequest {
    private Integer pageSize;
    private Integer pageIndex;
    private String state;
    private String sort;
    private String individualId;
}
