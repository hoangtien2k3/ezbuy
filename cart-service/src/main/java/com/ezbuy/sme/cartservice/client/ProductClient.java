package com.ezbuy.sme.cartservice.client;

import com.ezbuy.productmodel.response.ListProductOfferResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds);
}
