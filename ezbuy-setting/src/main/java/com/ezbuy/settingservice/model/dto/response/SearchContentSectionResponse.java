package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.ContentSectionDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchContentSectionResponse {
    private List<ContentSectionDTO> contentSection;
    private PaginationDTO pagination;
}
