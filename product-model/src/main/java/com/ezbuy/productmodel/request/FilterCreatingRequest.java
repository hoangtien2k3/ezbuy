package com.ezbuy.productmodel.request;

import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FilterCreatingRequest {
    @NotEmpty(message = "telecom.service.id.required")
    private String telecomServiceId;
    @NotEmpty(message = "filter.criteria.required")
    private List<ProductSpecCharAndValDTO> productSpecCharList;
}
