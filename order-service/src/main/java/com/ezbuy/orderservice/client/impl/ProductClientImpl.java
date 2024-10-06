package com.ezbuy.orderservice.client.impl;

import com.ezbuy.orderservice.client.ProductClient;
import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActive;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class ProductClientImpl implements ProductClient {

    private final BaseRestClient baseRestClient;

    private final WebClient productClient;

    public ProductClientImpl(BaseRestClient baseRestClient,
                             @Qualifier("productClient") WebClient product) {
        this.baseRestClient = baseRestClient;
        this.productClient = product;
    }

    @Override
    public Mono<List<ProductOfferTemplateDTO>> getProductInfo(List<String> templateIds) {
        FilterProductTemplateDTO filterProductTemplateDTO = new FilterProductTemplateDTO();
        filterProductTemplateDTO.setListId(templateIds);

        return baseRestClient.post(productClient, "/v1/filter-product-template", null, filterProductTemplateDTO, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get().getData() == null) {
                        return new ArrayList<>();
                    }

                    Map<String, Object> dataMap = (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap.get("data"));
                    return DataUtil.parseStringToObject(dataJson, new TypeReference<List<ProductOfferTemplateDTO>>() {
                    }, new ArrayList<>());
                }).onErrorResume(throwable -> Mono.just(new ArrayList<>()));
    }

    @Override
    public Mono<List<Subscriber>> getListSubscriberActive(String idNo, List<String> lstTelecomServiceId) {
        FilterGetListSubscriberActive filterGetListSubscriberActive = new FilterGetListSubscriberActive();
        filterGetListSubscriberActive.setIdNo(idNo);
        filterGetListSubscriberActive.setLstTelecomServiceId(lstTelecomServiceId);

        return baseRestClient.post(productClient, "/v1/get-list-subscriber-active", null, filterGetListSubscriberActive, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get().getData() == null) {
                        return new ArrayList<>();
                    }

                    Map<String, Object> dataMap = (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap.get("data"));
                    return DataUtil.parseStringToObject(dataJson, new TypeReference<List<Subscriber>>() {
                    }, new ArrayList<>());
                }).onErrorResume(throwable -> Mono.just(new ArrayList<>()));
    }

    private String joinStringList(List<String> inputList) {
        if (DataUtil.isNullOrEmpty(inputList)) {
            return "";
        }
        return inputList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public Mono<List<Telecom>> getTelecomByAlias(List<String> lstTelecomServiceAlias) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("lstTelecomServiceAlias", joinStringList(lstTelecomServiceAlias));
        return baseRestClient.get(productClient, "/v1/telecom-service/get-by-lst-alias", null, params, DataResponse.class)
                .flatMap(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.just(new ArrayList<>());
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.just(new ArrayList<>());
                    }

                    String dataJson = DataUtil.parseObjectToString(dataResponseOptional.get().getData());

                    List<Telecom> lstTelecom = DataUtil.parseStringToObject(
                            dataJson,
                            new TypeReference<>() {
                            },
                            new ArrayList<>()
                    );
                    return Mono.just(lstTelecom);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws product getTelecomByAlias error: {}", throwable);
                    return new ArrayList<>();
                });

    }
}
