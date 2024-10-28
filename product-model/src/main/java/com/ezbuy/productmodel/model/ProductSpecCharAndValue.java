package com.ezbuy.productmodel.model;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecCharAndValue extends ProductSpecChar {
    private UUID valId;
    private UUID valProductSpecCharId;
    private String valValue;
    private String valName;
    private Integer valStatus;
    private Integer valState;
    private Integer valOrderDisplay;

    @Builder
    public ProductSpecCharAndValue(
            String id,
            String code,
            String name,
            String telecomServiceId,
            String telecomServiceAlias,
            Integer orderDisplay,
            UUID valId,
            UUID valProductSpecCharId,
            String valValue,
            String valName,
            Integer valOrderDisplay,
            String viewType) {
        super(id, code, name, telecomServiceId, telecomServiceAlias, 1, 1, orderDisplay, viewType, true);
        this.valId = valId;
        this.valProductSpecCharId = valProductSpecCharId;
        this.valValue = valValue;
        this.valName = valName;
        this.valOrderDisplay = valOrderDisplay;
    }
}
