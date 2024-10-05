package com.ezbuy.ordermodel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PricingProductRequest {
    private String organizationId;
    private CustomerPricingDTO customer;
    private List<ProductOrderItemDTO> productOrderItem;
    private String viewMode = "NONE";
    private String extFilter = "{\"businessCode\":\"EXTEND_PACKAGE\"}";
}
