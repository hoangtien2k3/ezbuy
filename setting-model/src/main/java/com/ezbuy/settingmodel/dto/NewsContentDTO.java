package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsContentDTO {
    private String id;
    private String content; //noi dung
    private Integer status; //trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String newsInfoId; //id thong tin tin tuc
    private String createBy;
    private LocalDateTime createAt;
    private String updateBy;
    private LocalDateTime updateAt;
}
