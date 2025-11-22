package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.entity.Telecom;
import java.util.List;
import lombok.Data;

@Data
public class TelecomPagingResponse {
    private List<Telecom> telecoms;
    private PaginationDTO pagination;
}
