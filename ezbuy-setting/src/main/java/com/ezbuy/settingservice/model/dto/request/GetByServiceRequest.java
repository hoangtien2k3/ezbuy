package com.ezbuy.settingservice.model.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class GetByServiceRequest {
    private List<String> lstServiceId;
}
