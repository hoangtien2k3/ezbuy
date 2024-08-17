package com.ezbuy.settingmodel.dto;

import com.ezbuy.settingmodel.model.MarketPage;
import lombok.Data;

@Data
public class MarketPageDTO extends MarketPage {
    private String nameService;
}
