package com.ezbuy.productmodel.request;

import lombok.Data;

@Data
public class ServiceGroupRequest {
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
