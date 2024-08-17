package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchOptionSetValueResponse {
    private List<OptionSetValueDTO> lstOptionSetValueDTO;
    private PaginationDTO pagination; //thong tin phan trang
}
