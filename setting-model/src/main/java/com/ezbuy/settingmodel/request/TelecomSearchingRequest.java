package com.ezbuy.settingmodel.request;

import lombok.Data;

@Data
public class TelecomSearchingRequest extends PageRequest{
    private String name;
}
