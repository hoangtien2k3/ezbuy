package com.ezbuy.productservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.dto.FilterProductTemplateDTO;
import reactor.core.publisher.Mono;

public interface ProductOfferTemplateService {

    Mono<DataResponse> getProductTemplate(FilterProductTemplateDTO filterProductTemplateDTO);

    Mono<DataResponse> getProductsForMegaMenu();
}
