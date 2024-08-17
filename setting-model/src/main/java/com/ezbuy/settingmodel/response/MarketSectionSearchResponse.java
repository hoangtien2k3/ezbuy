package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketSection;
import lombok.Data;

import java.util.List;

@Data
public class MarketSectionSearchResponse {
    private List<MarketSection> marketSection;
    private PaginationDTO pagination;
}
