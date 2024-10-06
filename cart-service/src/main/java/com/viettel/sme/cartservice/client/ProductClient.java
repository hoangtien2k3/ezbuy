package com.viettel.sme.cartservice.client;

import com.ezbuy.productmodel.response.ListProductOfferResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductClient {
    Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds);
}
