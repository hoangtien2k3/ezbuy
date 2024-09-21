package com.ezbuy.settingmodel.dto;

import com.ezbuy.settingmodel.model.ContentSection;
import lombok.Data;

@Data
public class ContentSectionDTO extends ContentSection {
    private String nameService;
    private String nameSection;
}
