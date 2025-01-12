package com.ezbuy.productmodel.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIgnoreProperties
public class ProductSpecCharV2DTO {
    private String code;
    private String name;
    private String valueType;
    private List<ProductSpecCharValueV2DTO> productSpecCharValueDTOList;
}
