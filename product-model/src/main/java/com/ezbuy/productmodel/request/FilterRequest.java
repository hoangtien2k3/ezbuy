package com.ezbuy.productmodel.request;

import com.ezbuy.productmodel.constants.enumeration.Operator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class FilterRequest {
    private Operator operator;
    private String entity;
    private String property;
    private String valueText;
    private String lstValue;
}
