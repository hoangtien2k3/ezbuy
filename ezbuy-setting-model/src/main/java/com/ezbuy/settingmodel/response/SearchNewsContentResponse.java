package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.NewsContentDTO;
import java.util.List;
import lombok.Data;

@Data
public class SearchNewsContentResponse {
    private List<NewsContentDTO> lstNewsContentDTO; // danh sach thong tin tin tuc
}
