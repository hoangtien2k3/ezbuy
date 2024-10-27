package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;
import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface ProductClient {
    Flux<IdentityProductPrice> getExProductPrices(Set<String> templateIds);

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId);
}
