package com.ezbuy.ordermodel.dto;

import lombok.Data;

@Data
public class SignerInfoListDTO {
    private String username;
    private String signRole;
    private String processType;
    private String order;
    private String requiredCa;
}
