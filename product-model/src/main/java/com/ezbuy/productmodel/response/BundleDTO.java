package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BundleDTO {
    private String code;
    private Integer productOfferingId;
    private ProductSpecificationDTO productSpecificationDTO;
    private String productOfferRelationId;
    private String mainOfferId;
    private String name;
    private String telecomServiceId;
}
