package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;
import com.ezbuy.paymentservice.client.ProductClient;
import com.ezbuy.core.client.BaseRestClient;

import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class ProductClientImpl implements ProductClient {

    @Qualifier("productClient")
    private final WebClient webClient;

    private final BaseRestClient baseRestClient;

    @Override
    public Flux<IdentityProductPrice> getExProductPrices(Set<String> templateIds) {
        return webClient
                .get()
                .uri(ClientUris.Product.GET_PRODUCT_PRICES)
                .retrieve()
                .bodyToFlux(IdentityProductPrice.class)
                .doOnError(err -> {
                    log.error("getExProductPrices error ", err);
                });
    }
}
