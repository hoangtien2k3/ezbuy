package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketSection;
import java.util.List;
import lombok.Data;

@Data
public class MarketSectionSearchResponse {
    private List<MarketSection> marketSection;
    private PaginationDTO pagination;
}
