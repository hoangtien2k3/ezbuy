package com.ezbuy.settingservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewsContentRequest {
    private String newsInfoId; // id thong tin tin tuc
    private String content; // ten nhom tin tuc
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
}
