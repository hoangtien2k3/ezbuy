package com.ezbuy.productservice.service;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.sme.productmodel.request.GetProductTemplateDetailRequest;
import reactor.core.publisher.Mono;

public interface PublicService {
    Mono<DataResponse> getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest request);
    Mono<DataResponse> getListTemplateComboForHubSme(String productOfferingId);
    Mono<DataResponse> getProductTemplateDetail(GetProductTemplateDetailRequest getProductTemplateDetailRequest);
}
