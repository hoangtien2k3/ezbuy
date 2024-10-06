package com.ezbuy.paymentmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@MappedSuperclass
@SuperBuilder
public class TelecomDTO {
    private String id;
    private String name;
    private String serviceAlias;
    private String description;
    private String image;
    private String originId;
    private Integer status;
    private Boolean isFilter;
    private String deployOrderCode;
    private Boolean connectOne;
}
