package com.ezbuy.cartservice.client;

import com.ezbuy.productmodel.dto.response.ListProductOfferResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ProductClient {
    Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds);
}
