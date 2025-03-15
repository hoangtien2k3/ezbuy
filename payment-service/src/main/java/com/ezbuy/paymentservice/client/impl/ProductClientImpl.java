package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;
import com.ezbuy.paymentservice.client.ProductClient;
import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.dto.response.ProductOfferTemplateDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds) {
        FilterProductTemplateDTO filterProductTemplateDTO = new FilterProductTemplateDTO();
        filterProductTemplateDTO.setListId(templateIds);
        return baseRestClient
                .post(webClient, "/v1/filter-product-template", null, filterProductTemplateDTO, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get().getData() == null) {
                        return new ArrayList<>();
                    }

                    Map<String, Object> dataMap =
                            (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap.get("data"));
                    return DataUtil.parseStringToObject(
                            dataJson, new TypeReference<List<ProductOfferTemplateDTO>>() {}, new ArrayList<>());
                })
                .onErrorResume(throwable -> Mono.just(new ArrayList<>()));
    }
}
