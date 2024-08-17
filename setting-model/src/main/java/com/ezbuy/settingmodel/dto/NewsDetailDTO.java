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
public class NewsDetailDTO {
    private String title; //tieu de tin tuc
    private String groupId; //id loai tin tuc
    private String groupName; //ten loai tin tuc
    private LocalDateTime createAt; //ngay dang tin
    private String content; //noi dung tin tuc
}
