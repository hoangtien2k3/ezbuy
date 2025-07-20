package com.ezbuy.productmodel.dto.response;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class ListProductOfferResponse {

    @XmlElement(name = "data")
    private List<ProductOfferTemplateDTO> data;

    @XmlElement(name = "utils")
    private TemplateOfferUtils utils;
}
