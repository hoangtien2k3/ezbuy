package com.ezbuy.settingmodel.dto.response;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.UploadImagesDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchImageResponse {
    private List<UploadImagesDTO> content;
    private PaginationDTO pagination;
}
