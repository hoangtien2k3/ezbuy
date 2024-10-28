package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDTO {
    private String groupId;
    private String groupName;
    private Integer order;
    private String originId;
    private String telecomServiceAlias; // bo sung alias cho PYC Scontract
}
