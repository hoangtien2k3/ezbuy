package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.ProductSpecificationRequest;
import com.ezbuy.productmodel.dto.response.SubscriberResponse;
import com.reactify.model.response.DataResponse;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

public interface RenewalCAService {

    Mono<DataResponse> getStatisticSubscriber(String organizationId, Integer time);

    Mono<DataResponse> getListSubscriber(
            @RequestParam(required = false) Long telecomServiceId,
            @RequestParam(required = false) String telecomServiceAlias,
            @RequestParam(required = true) String organizationId);

    Mono<DataResponse> syncListSubscriber();

    Mono<DataResponse> getProductSpecification(ProductSpecificationRequest productSpecificationRequest);

    Mono<List<SubscriberResponse>> getSubscriberSmeInfo(
            Long telecomServiceId, String idNo, String isdn, String telecomServiceAlias);
}
