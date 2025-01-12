package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.productmodel.dto.request.GetProductTemplateDetailRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface PublicService {
    Mono<DataResponse> getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest request);

    Mono<DataResponse> getListTemplateComboForHubSme(String productOfferingId);

    Mono<DataResponse> getProductTemplateDetail(GetProductTemplateDetailRequest getProductTemplateDetailRequest);
}
