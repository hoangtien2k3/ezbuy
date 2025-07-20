package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
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
public class SearchMarketInfoResponse {
    private List<MarketInfoDTO> content;
    private PaginationDTO pagination;
}
