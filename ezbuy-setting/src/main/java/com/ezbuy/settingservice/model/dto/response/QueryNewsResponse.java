package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.ContentNewsDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class QueryNewsResponse {
    private List<ContentNewsDTO> content;
    private PaginationDTO pagination;
}
