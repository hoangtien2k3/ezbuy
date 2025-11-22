package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MarketPageSectionRequest {
    private String pageId;
    private String sectionId;
    private Integer displayOrder;
    private Integer status;
}
