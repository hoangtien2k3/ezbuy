package com.ezbuy.productmodel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDTO {
    private List<ProductSpecCharUseV2DTO> lstProductSpecCharUseDTO;
}
