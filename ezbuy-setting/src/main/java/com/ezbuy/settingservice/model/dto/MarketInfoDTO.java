package com.ezbuy.settingservice.model.dto;

import com.ezbuy.settingservice.model.entity.MarketInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MarketInfoDTO extends MarketInfo {
    private String nameService;
    private String base64;
}
