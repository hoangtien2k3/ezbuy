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
public class GroupNewsDTO {
    private String id;
    private String name; //ten nhom tin tuc
    private String code; //ma nhom tin tuc
    private Integer displayOrder; //thu tu sap xep
    private Integer status; //trang thai: 1 - hieu luc, 0 - khong hieu luc
    private String createBy;
    private LocalDateTime createAt;
    private String updateBy;
    private LocalDateTime updateAt;
    private String groupNewsOrder; //thu tu hien thi bang group_news
    private String groupNewsId;
    private String summary;
    private String title;
}
