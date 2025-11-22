package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.entity.MarketSection;
import java.util.List;
import lombok.Data;

@Data
public class MarketSectionSearchResponse {
    private List<MarketSection> marketSection;
    private PaginationDTO pagination;
}
