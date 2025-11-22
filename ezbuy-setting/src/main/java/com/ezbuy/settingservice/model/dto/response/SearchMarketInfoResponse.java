package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.MarketInfoDTO;
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
public class SearchMarketInfoResponse {
    private List<MarketInfoDTO> content;
    private PaginationDTO pagination;
}
