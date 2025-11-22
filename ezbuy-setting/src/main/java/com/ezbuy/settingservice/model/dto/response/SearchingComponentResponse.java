package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchingComponentResponse {
    private List<ContentDisplayDTO> contentDisplays;
    private PaginationDTO pagination;
}
