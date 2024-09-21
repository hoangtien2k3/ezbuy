package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.Telecom;
import java.util.List;
import lombok.Data;

@Data
public class TelecomPagingResponse {
    private List<Telecom> telecoms;
    private PaginationDTO pagination;
}
