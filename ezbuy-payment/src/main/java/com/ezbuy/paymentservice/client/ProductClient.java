package com.ezbuy.paymentservice.client;

import java.util.Set;

import com.ezbuy.paymentservice.model.dto.response.IdentityProductPrice;
import reactor.core.publisher.Flux;

public interface ProductClient {

    Flux<IdentityProductPrice> getExProductPrices(Set<String> templateIds);
}
