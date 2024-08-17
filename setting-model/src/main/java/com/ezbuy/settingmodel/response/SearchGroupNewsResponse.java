package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.GroupNewsDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchGroupNewsResponse {
    private List<GroupNewsDTO> lstGroupNewsDTO; //danh sach nhom tin
    private PaginationDTO pagination; //thong tin phan trang
}
