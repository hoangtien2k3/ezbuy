package com.ezbuy.settingmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideDTO {
    private List<MediaDTO> medias;
    private Long autoNext;
    private Long delayTime;
}
