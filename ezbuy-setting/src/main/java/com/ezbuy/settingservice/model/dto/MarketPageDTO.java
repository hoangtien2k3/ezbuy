package com.ezbuy.settingservice.model.dto;

import com.ezbuy.settingservice.model.entity.MarketPage;
import lombok.Data;

@Data
public class MarketPageDTO extends MarketPage {
    private String nameService;
}
