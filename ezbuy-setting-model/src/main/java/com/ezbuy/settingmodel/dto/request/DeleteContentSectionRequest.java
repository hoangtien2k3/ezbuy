package com.ezbuy.settingmodel.dto.request;

import lombok.Data;

@Data
public class DeleteContentSectionRequest {
    private String id;
    private String parentId;
}
