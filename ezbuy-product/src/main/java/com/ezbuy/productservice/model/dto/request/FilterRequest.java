package com.ezbuy.productservice.model.dto.request;

import com.ezbuy.productservice.constants.enumeration.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FilterRequest {
    private Operator operator;
    private String entity;
    private String property;
    private String valueText;
    private String lstValue;
}
