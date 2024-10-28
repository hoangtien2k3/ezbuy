package com.ezbuy.ordermodel.dto.pricing;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "return")
public class PricingMessage {
    private String errorCode;
    private String description;
    private boolean success;
    private List<ProductOrderItem> productOrderItem;
    private OrderPrice priceSummary;
}
