package com.ezbuy.settingservice.model.dto;

import com.ezbuy.settingservice.model.entity.ContentDisplay;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDisplayDTO extends ContentDisplay {
    private List<ContentDisplayDTO> contentDisplayDTOList;
}
