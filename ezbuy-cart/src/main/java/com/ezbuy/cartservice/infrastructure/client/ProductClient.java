package com.ezbuy.cartservice.infrastructure.client;

import java.util.List;

import com.ezbuy.cartservice.application.dto.response.ListProductOfferResponse;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds);
}
