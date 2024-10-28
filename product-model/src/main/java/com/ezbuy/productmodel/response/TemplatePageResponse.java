package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.request.ApiUtils;
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
