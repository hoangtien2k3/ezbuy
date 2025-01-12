package com.ezbuy.productmodel.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecificationDTO {
    private List<ProductSpecCharUseV2DTO> lstProductSpecCharUseDTO;
}
