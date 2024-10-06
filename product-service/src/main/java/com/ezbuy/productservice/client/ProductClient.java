package com.ezbuy.productservice.client;

import com.ezbuy.productmodel.request.ApiUtils;
import com.ezbuy.productmodel.request.GetListProductOfferingComboForHubSmeRequest;
import com.ezbuy.productmodel.request.GetProductTemplateDetailRequest;
import com.ezbuy.productmodel.response.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ProductClient {

    Mono<Optional<ListProductOfferResponse>> getListProductOfferTemplateByListIds(List<String> ids);

    Mono<Optional<ListProductOfferResponse>> getLstTemplateOffer(String telecomServiceId, ApiUtils utils, List<String> priceTypes);

    Mono<Optional<LstServiceCharacteristicResponse>> getLstProductSpec(String telecomServiceId);

    Mono<Optional<ProductOfferingSpecificationResponse>> getProductOfferingSpecification(String productId);

    Mono<Optional<GetListProductOfferingComboForHubSmeResponse>> getListProductOfferingComboForHubSme(GetListProductOfferingComboForHubSmeRequest request);

    Mono<Optional<GetListTemplateComboForHubSmeResponse>> getListTemplateComboForHubSme(String productOfferingId);

    Mono<Optional<GetProductTemplateDetailResponse>> getProductTemplateDetail(GetProductTemplateDetailRequest getProductTemplateDetailRequest);
}
