package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;

import java.util.Set;
import reactor.core.publisher.Flux;

public interface ProductClient {

    Flux<IdentityProductPrice> getExProductPrices(Set<String> templateIds);
}
