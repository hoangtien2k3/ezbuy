package com.ezbuy.settingservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelecomDTO {
    private String id;
    private String name;
    private String serviceAlias;
    private String description;
    private String image;
    private String originId;
    private Integer status;
    private Boolean isFilter;
    private String deployOrderCode;
    private Boolean connectOne;
}
