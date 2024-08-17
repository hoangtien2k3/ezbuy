package com.ezbuy.settingmodel.request.v2;

import lombok.Data;

import java.util.List;

@Data
public class GetByServiceRequestV2 {
    private List<String> lstAlias;
}
