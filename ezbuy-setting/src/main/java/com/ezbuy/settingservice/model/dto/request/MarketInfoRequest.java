package com.ezbuy.settingservice.model.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

// request to create or update MarketInfo
@Data
public class MarketInfoRequest {
    private String serviceId;
    private String serviceAlias; // alias of service PYCXXX/LuongToanTrinhScontract

    @Size(max = 500, message = "market.info.title.length.500.invalid")
    private String title;

    @Size(max = 500, message = "market.info.navigator.url.length.500.invalid")
    private String navigatorUrl;

    private Integer marketOrder;
    private String marketImageUrl;
    private String relatedTelecomServiceId;
    private Integer status;
    private String image;
    private String imageName;
}
