package com.ezbuy.productmodel.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties
public class ProductTemplateSpecCharDTO {
    private String code;
    private String name;
    private String value;
    private String valueType;
    private String valueName;
}
