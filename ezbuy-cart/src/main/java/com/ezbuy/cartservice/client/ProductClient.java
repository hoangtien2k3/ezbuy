package com.ezbuy.cartservice.client;

import java.util.List;

import com.ezbuy.cartservice.domain.dto.response.ListProductOfferResponse;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds);
}
