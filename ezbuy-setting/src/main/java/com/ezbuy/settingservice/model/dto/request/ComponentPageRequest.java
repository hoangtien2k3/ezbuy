package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;

@Data
public class ComponentPageRequest extends PageRequest {
    private String name;
}
