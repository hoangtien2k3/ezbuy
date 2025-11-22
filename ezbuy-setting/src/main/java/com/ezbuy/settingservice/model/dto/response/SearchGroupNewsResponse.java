package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.GroupNewsDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchGroupNewsResponse {
    private List<GroupNewsDTO> lstGroupNewsDTO; // danh sach nhom tin
    private PaginationDTO pagination; // thong tin phan trang
}
