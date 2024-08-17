package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.ContentSectionDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchContentSectionResponse {
    private List<ContentSectionDTO> contentSection;
    private PaginationDTO pagination;
}
