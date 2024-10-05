package com.ezbuy.ordermodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRelationshipDTO {
    private Long id;
    private Long relationshipId;
    private String relationshipType;
}
