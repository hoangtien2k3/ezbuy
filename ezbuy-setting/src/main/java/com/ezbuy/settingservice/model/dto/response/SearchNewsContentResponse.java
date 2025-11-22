package com.ezbuy.settingservice.model.dto.response;

import com.ezbuy.settingservice.model.dto.NewsContentDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchNewsContentResponse {
    private List<NewsContentDTO> lstNewsContentDTO; // danh sach thong tin tin tuc
}
