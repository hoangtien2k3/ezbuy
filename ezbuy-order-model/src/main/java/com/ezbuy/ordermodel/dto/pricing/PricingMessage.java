package com.ezbuy.ordermodel.dto.pricing;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@XmlRootElement(name = "return")
public class PricingMessage {
    private String errorCode;
    private String description;
    private boolean success;
    private List<ProductOrderItem> productOrderItem;
    private OrderPrice priceSummary;
}
