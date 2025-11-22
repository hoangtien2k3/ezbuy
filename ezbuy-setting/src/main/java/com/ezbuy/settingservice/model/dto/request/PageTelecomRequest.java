package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;

@Data
public class PageTelecomRequest {
    private Integer pageIndex;
    private Integer pageSize;
    private String serviceTypeId;
    private String serviceType;
    private String serviceName;
    private Integer status;
    private String sort;
}
