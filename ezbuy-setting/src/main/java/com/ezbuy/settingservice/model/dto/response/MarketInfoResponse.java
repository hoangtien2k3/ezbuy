package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.MarketInfoDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class MarketInfoResponse {
    List<MarketInfoDTO> marketInfoDTOList;
    private PaginationDTO pagination;
}
