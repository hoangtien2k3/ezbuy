package com.ezbuy.settingservice.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsContentDTO {
    private String id;
    private String content; // noi dung
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String newsInfoId; // id thong tin tin tuc
    private String createBy;
    private LocalDateTime createAt;
    private String updateBy;
    private LocalDateTime updateAt;
}
