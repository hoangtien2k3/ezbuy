package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface ProductOfferTemplateService {

    Mono<DataResponse> getProductTemplate(FilterProductTemplateDTO filterProductTemplateDTO);

    Mono<DataResponse> getProductsForMegaMenu();
}
