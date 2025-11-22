package com.ezbuy.settingservice.model.dto.request.v2;

import java.util.List;
import lombok.Data;

@Data
public class GetByServiceRequestV2 {
    private List<String> lstAlias;
}
