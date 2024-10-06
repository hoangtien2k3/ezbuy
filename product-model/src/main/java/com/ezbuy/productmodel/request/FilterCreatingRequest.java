package com.ezbuy.productmodel.request;

import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class FilterCreatingRequest {
    @NotEmpty(message = "telecom.service.id.required")
    private String telecomServiceId;
    @NotEmpty(message = "filter.criteria.required")
    private List<ProductSpecCharAndValDTO> productSpecCharList;
}
