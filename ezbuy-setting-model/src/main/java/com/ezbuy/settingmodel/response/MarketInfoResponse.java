package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class MarketInfoResponse {
    List<MarketInfoDTO> marketInfoDTOList;
    private PaginationDTO pagination;
}
