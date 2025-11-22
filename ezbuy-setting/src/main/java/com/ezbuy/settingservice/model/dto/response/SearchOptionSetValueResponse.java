package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.OptionSetValueDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchOptionSetValueResponse {
    private List<OptionSetValueDTO> lstOptionSetValueDTO;
    private PaginationDTO pagination; // thong tin phan trang
}
