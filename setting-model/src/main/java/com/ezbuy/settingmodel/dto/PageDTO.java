package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO {
    private String id;
    private String code;
    private Integer status;
    private String title;
    private String logoUrl;
    private String createBy;
    private String updateBy;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<ContentDisplayDTO> contentDisplayList;
}
