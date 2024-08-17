package com.ezbuy.settingmodel.response;

import com.ezbuy.settingmodel.dto.NewsContentDTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchNewsContentResponse {
    private List<NewsContentDTO> lstNewsContentDTO; //danh sach thong tin tin tuc
}
