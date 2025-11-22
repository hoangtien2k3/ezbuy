package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.MarketPageDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
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
