package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchingComponentResponse {
    private List<ContentDisplayDTO> contentDisplays;
    private PaginationDTO pagination;
}
