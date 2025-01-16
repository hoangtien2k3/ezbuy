package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketPageSection;
import lombok.Data;

import java.util.List;

@Data
public class SearchMarketPageSectionResponse {
    private List<MarketPageSection> lstMarketPageSection;
    private PaginationDTO pagination;
}
