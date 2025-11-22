package com.ezbuy.settingservice.model.dto;

import com.ezbuy.settingservice.model.entity.MarketSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketSectionDTO extends MarketSection {
    private String telecomServiceName;
}
