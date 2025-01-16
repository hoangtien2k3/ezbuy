package com.ezbuy.settingmodel.dto.request;

import lombok.Data;

@Data
public class SearchMarketPageSectionRequest {
    private String id;
    private String pageId;
    private String status;
    private String sort;
    private Integer pageIndex;
    private Integer pageSize;
}
