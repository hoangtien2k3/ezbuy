package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentmodel.dto.response.IdentityProductPrice;
import com.ezbuy.paymentservice.client.ProductClient;
import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActive;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import java.util.*;
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
public class ProductClientImpl implements ProductClient {

    private final WebClient webClient;
    private final BaseRestClient baseRestClient;

    public ProductClientImpl(BaseRestClient baseRestClient, @Qualifier("product") WebClient product) {
        this.baseRestClient = baseRestClient;
        this.webClient = product;
    }

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

    @Override
    public Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId) {
        FilterGetListSubscriberActive filterGetListSubscriberActive = new FilterGetListSubscriberActive();
        filterGetListSubscriberActive.setIdNo(idNo);
        filterGetListSubscriberActive.setLstTelecomServiceId(lstTelecomServiceId);

        return baseRestClient
                .post(
                        webClient,
                        "/v1/get-list-subscriber-active",
                        null,
                        filterGetListSubscriberActive,
                        DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get().getData() == null) {
                        return new ArrayList<>();
                    }

                    Map<String, Object> dataMap =
                            (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap.get("data"));
                    return DataUtil.parseStringToObject(
                            dataJson, new TypeReference<List<Subscriber>>() {}, new ArrayList<>());
                })
                .onErrorResume(throwable -> Mono.just(new ArrayList<>()));
    }
}
