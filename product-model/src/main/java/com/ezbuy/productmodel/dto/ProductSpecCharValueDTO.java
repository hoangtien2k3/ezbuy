package com.ezbuy.productmodel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecCharValueDTO {

    private String id;
    private String productSpecCharId;
    private String value;
    private String name;
    private Integer displayOrder;
    private Integer state;
    private Integer status;
}
