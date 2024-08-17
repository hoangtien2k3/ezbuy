package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.OptionSetDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchOptionSetResponse {
    private List<OptionSetDTO> lstOptionSetDTO;
    private PaginationDTO pagination; //thong tin phan trang
}
