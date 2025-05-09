package com.ezbuy.ordermodel.dto.pricing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderPrice {
    String nameForDisplay;
    String nameForInvoice;
    Long productOfferPriceId;
    Long originalPrice;
    Long price;
    Long discount;
    Long vat;
    String unitName;
    Long quantity;
    String priceTypeCode;
    String valueCharacteristic;
    String productOrderItemId;
    String type; // phan loai gia de len hoa don
    String notDisplay;
    Long priceDay;
}
