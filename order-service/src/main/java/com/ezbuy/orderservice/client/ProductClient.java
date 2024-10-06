package com.ezbuy.orderservice.client;

import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductClient {

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId);

    Mono<List<Telecom>> getTelecomByAlias(List<String> lstTelecomServiceId);

}
