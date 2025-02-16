package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;
import com.ezbuy.productmodel.dto.response.ProductOfferTemplateDTO;
import java.util.List;
import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Flux<IdentityProductPrice> getExProductPrices(Set<String> templateIds);

    Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds);

    // Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String>
    // lstTelecomServiceId);
}
