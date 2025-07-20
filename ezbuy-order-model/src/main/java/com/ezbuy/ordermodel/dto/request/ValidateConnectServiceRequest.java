package com.ezbuy.ordermodel.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class ValidateConnectServiceRequest {
    private List<String> lstServiceId;
    private String individualId;
}
