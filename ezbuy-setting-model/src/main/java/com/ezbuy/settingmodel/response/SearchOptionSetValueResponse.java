package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchOptionSetValueResponse {
    private List<OptionSetValueDTO> lstOptionSetValueDTO;
    private PaginationDTO pagination; // thong tin phan trang
}
