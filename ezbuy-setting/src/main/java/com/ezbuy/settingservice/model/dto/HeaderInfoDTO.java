package com.ezbuy.settingservice.model.dto;

import lombok.Data;

@Data
public class HeaderInfoDTO {
    private String serviceId;
    private String nameService;
    private String iconUrl;
    private String slogan;
    private String copyright;
    private Long enableBuyNow;
    private Long enableTrial;
    private Long enablePreOrder;
}
