package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.entity.Page;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchingPageResponse {
    private List<Page> pages;
    private PaginationDTO pagination;
}
