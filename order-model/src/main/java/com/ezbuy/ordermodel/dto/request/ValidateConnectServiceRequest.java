package com.ezbuy.ordermodel.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ValidateConnectServiceRequest {
    private List<String> lstServiceId;
    private String individualId;
}
