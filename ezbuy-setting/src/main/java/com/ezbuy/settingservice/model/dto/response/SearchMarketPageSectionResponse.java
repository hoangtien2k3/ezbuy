package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.entity.MarketPageSection;
import java.util.List;
import lombok.Data;

@Data
public class SearchMarketPageSectionResponse {
    private List<MarketPageSection> lstMarketPageSection;
    private PaginationDTO pagination;
}
