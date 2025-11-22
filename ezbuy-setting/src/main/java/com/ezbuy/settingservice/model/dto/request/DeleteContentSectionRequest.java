package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;

@Data
public class DeleteContentSectionRequest {
    private String id;
    private String parentId;
}
