package com.ezbuy.settingmodel.dto;

import com.ezbuy.settingmodel.model.MarketSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketSectionDTO extends MarketSection {
    private String telecomServiceName;
}
