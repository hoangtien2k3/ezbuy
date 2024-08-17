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
public class RelateNewsDTO {
    private String id; //id ban ghi tin tuc trong bang news_info
    private String navigatorUrl; //Link image tin tuc
    private String groupName; //ten loai tin tuc
    private LocalDateTime createAt; //ngay dang tin
    private String summary; //noi dung tin tuc
    private String title; //title tin tuc
}
