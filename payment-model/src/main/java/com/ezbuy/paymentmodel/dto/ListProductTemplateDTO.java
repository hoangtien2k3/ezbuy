package com.ezbuy.paymentmodel.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListProductTemplateDTO {
    private String templateCode;
    private Long quantity;
    private List<ProductCharacteristicDTO> productCharacteristic;
}
