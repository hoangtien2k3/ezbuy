package com.ezbuy.settingmodel.request;

import lombok.Data;

import java.util.List;

@Data
public class GetByServiceRequest {
    private List<String> lstServiceId;
}
