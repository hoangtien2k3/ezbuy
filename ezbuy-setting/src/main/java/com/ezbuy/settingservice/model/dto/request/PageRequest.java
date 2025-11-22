package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;

@Data
public class PageRequest {
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
