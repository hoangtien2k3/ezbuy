package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
    private String type;
    private String mediaId;
    private String url;
    private Long order;
    private String redirectLink;
}
