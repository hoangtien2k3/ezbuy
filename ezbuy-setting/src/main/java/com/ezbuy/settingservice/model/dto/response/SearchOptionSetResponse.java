package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.OptionSetDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchOptionSetResponse {
    private List<OptionSetDTO> lstOptionSetDTO;
    private PaginationDTO pagination; // thong tin phan trang
}
