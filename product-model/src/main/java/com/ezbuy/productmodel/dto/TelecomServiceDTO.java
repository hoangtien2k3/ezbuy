package com.ezbuy.productmodel.dto;

import lombok.Data;

@Data
public class TelecomServiceDTO {
    Long telecomServiceId;
    String name;
    String serviceAlias;
    ProductOfferingDTO lstTelecomServiceDTO;
}
