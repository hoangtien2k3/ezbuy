package com.ezbuy.productmodel.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties
public class ProductSpecCharUseDTO {

    private ProductSpecCharDTO productSpecCharDTO;
}
