package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketPageSection;
import java.util.List;
import lombok.Data;

@Data
public class SearchMarketPageSectionResponse {
    private List<MarketPageSection> lstMarketPageSection;
    private PaginationDTO pagination;
}
