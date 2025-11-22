package com.ezbuy.settingservice.model.dto.request;

import java.util.List;
import lombok.Data;

// request for Setting-006 Ham lay cau hinh huong dan va tai nguyen theo List sectionId (content_section)
@Data
public class GetBySectionRequest {
    private List<String> lstSectionId; // danh sach sectionId
}
