package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchingComponentResponse {
    private List<ContentDisplayDTO> contentDisplays;
    private PaginationDTO pagination;
}
