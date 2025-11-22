package com.ezbuy.productservice.model.dto;

import com.ezbuy.productservice.model.dto.request.ApiUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterProductTemplateDTO {
    private String telecomServiceAlias;
    private ApiUtils utils;
    private List<String> listId;
    private List<String> priceTypes;
}
