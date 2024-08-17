package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.Telecom;
import lombok.Data;

import java.util.List;

@Data
public class TelecomPagingResponse {
    private List<Telecom> telecoms;
    private PaginationDTO pagination;
}
