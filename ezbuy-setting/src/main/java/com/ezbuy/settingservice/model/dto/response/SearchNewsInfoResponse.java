package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.NewsInfoDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchNewsInfoResponse {
    private List<NewsInfoDTO> lstNewsInfoDTO; // danh sach thong tin tin tuc
    private PaginationDTO pagination; // thong tin phan trang
}
