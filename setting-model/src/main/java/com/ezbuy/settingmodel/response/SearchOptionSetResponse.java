package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchOptionSetResponse {
    private List<OptionSetDTO> lstOptionSetDTO;
    private PaginationDTO pagination; // thong tin phan trang
}
