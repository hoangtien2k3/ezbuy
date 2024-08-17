package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class MarketInfoResponse {
    List<MarketInfoDTO> marketInfoDTOList;
    private PaginationDTO pagination;
}
