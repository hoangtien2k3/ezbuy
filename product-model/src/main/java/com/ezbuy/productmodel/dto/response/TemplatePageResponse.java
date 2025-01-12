package com.ezbuy.productmodel.dto.response;

import com.ezbuy.productmodel.dto.request.ApiUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplatePageResponse {
    private List<ProductOfferTemplateDTO> listRsp;
    private ApiUtils utils;
}
