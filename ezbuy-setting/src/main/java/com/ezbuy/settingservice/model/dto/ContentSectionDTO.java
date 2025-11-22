package com.ezbuy.settingservice.model.dto;

import com.ezbuy.settingservice.model.entity.ContentSection;
import lombok.Data;

@Data
public class ContentSectionDTO extends ContentSection {
    private String nameService;
    private String nameSection;
}
