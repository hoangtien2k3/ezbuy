package com.ezbuy.productmodel.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecificationRequest {
    private List<String> productIds;
    private Long telecomServiceId;
    private String telecomServiceAlias;
    private Integer groupType;
    private String organizationId;
}
