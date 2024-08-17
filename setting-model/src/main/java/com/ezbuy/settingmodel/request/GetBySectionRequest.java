package com.ezbuy.settingmodel.request;

import lombok.Data;

import java.util.List;

// request for Setting-006 Ham lay cau hinh huong dan va tai nguyen theo List sectionId (content_section)
@Data
public class GetBySectionRequest {
    private List<String> lstSectionId; // danh sach sectionId
}
