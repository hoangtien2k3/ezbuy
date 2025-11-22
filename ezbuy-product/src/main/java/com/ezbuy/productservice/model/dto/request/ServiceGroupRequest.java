package com.ezbuy.productservice.model.dto.request;

import lombok.Data;

@Data
public class ServiceGroupRequest {
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
