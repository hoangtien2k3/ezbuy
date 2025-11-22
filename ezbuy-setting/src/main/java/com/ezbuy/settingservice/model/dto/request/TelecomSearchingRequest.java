package com.ezbuy.settingservice.model.dto.request;

import lombok.Data;

@Data
public class TelecomSearchingRequest extends PageRequest {
    private String name;
}
