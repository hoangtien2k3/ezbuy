package com.ezbuy.productmodel.response;

import com.ezbuy.productmodel.request.ApiUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplatePageResponse {
    private List<ProductOfferTemplateDTO> listRsp;
    private ApiUtils utils;
}
