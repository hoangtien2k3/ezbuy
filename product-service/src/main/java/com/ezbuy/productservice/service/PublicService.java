package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.productmodel.request.GetProductTemplateDetailRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface PublicService {
    Mono<DataResponse> getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest request);

    Mono<DataResponse> getListTemplateComboForHubSme(String productOfferingId);

    Mono<DataResponse> getProductTemplateDetail(GetProductTemplateDetailRequest getProductTemplateDetailRequest);
}
