package com.ezbuy.settingmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchContentSectionRequest extends PageRequest {
    private String sectionId;
    private String type;
    private String refId;
    private String refAlias;
    private String refType;
    private String name;
}
