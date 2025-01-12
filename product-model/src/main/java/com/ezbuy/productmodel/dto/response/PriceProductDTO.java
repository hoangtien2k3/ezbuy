package com.ezbuy.productmodel.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceProductDTO {
    private String type;
    private Integer value;
    private String dataType;
    private Long price;
}
