package com.ezbuy.settingmodel.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsInfoDTO {
    private String id;
    private String title; // tieu de
    private String code; // ma thong tin
    private Integer displayOrder; // thu tu sap xep
    private Integer status; // trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String groupNewsId; // id nhom tin tuc
    private String summary; // tom tat noi dung
    private String navigatorUrl; // anh dai dien
    private String state; // trang thai nghiep vu
    private String createBy;
    private LocalDateTime createAt;
    private String updateBy;
    private LocalDateTime updateAt;
    private String groupNewsName;
    private String groupNewsCode;
    private String groupNewsOrder; // thu tu hien thi bang group_news
}
