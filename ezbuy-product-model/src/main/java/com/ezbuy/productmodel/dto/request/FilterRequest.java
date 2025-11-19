package com.ezbuy.productmodel.dto.request;

import com.ezbuy.productmodel.constants.enumeration.Operator;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
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
