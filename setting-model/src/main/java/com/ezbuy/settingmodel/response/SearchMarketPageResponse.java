package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.MarketPageDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchMarketPageResponse {
    private List<MarketPageDTO> marketPage;
    private PaginationDTO pagination; // phan trang
}
