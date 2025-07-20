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
public class NewsDetailDTO {
    private String title; // tieu de tin tuc
    private String groupId; // id loai tin tuc
    private String groupName; // ten loai tin tuc
    private LocalDateTime createAt; // ngay dang tin
    private String content; // noi dung tin tuc
}
