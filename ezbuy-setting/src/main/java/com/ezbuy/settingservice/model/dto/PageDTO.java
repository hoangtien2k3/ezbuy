package com.ezbuy.settingservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
