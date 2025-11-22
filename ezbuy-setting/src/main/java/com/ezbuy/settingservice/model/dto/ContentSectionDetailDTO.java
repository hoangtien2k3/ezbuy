package com.ezbuy.settingservice.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentSectionDetailDTO {
    private String id;
    private String sectionId;
    private String type;
    private String refId;
    private String refAlias; // Setting-008 add alias
    private String refType;
    private String name;
    private Long order;
    private String parentId;
    private String status;
    private String sectionName;
}
