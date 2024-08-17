package com.ezbuy.settingmodel.dto.response;

import com.ezbuy.settingmodel.dto.ContentNewsDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class QueryNewsResponse {
    private List<ContentNewsDTO> content;
    private PaginationDTO pagination;
}
