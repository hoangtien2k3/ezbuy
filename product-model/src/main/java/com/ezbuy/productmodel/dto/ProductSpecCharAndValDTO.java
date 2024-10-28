package com.ezbuy.productmodel.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecCharAndValDTO {
    private String id;
    private String code;
    private String name;
    private String telecomServiceId;
    private String telecomServiceAlias;
    private Integer displayOrder;
    private Integer state;
    private Integer status;
    private String viewType;
    private List<ProductSpecCharValueDTO> productSpecCharValueDTOList;

    public void addValues(List<ProductSpecCharValueDTO> values) {
        if (productSpecCharValueDTOList == null) {
            productSpecCharValueDTOList = new ArrayList<>();
        }
        if (values != null) {
            productSpecCharValueDTOList.addAll(values);
        }
    }
}
