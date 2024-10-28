package com.ezbuy.productmodel.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
