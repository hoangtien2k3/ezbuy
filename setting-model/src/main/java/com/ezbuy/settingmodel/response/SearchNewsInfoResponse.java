package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.NewsInfoDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchNewsInfoResponse {
    private List<NewsInfoDTO> lstNewsInfoDTO; // danh sach thong tin tin tuc
    private PaginationDTO pagination; // thong tin phan trang
}
