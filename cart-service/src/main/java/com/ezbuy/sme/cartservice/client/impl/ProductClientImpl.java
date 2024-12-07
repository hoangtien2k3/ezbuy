package com.ezbuy.sme.cartservice.client.impl;

import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.response.ListProductOfferResponse;
import com.ezbuy.sme.cartservice.client.ProductClient;
import com.ezbuy.sme.cartservice.client.properties.ProductProperties;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("webClientFactory")
public class ProductClientImpl implements ProductClient {

    @Qualifier("product")
    private final WebClient product;
    private final BaseRestClient baseRestClient;

    private final ProductProperties productProperties;

    @Override
    public Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds) {
        FilterProductTemplateDTO filterProductTemplateDTO = new FilterProductTemplateDTO();
        filterProductTemplateDTO.setListId(templateIds);
        return baseRestClient
                .post(product, "/v1/filter-product-template", null, filterProductTemplateDTO, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("server.error")));
                    }

                    String jsonValue =
                            DataUtil.parseObjectToString(respOptional.get().getData());
                    ListProductOfferResponse listProductOfferResponse =
                            DataUtil.parseStringToObject(jsonValue, ListProductOfferResponse.class);
                    if (listProductOfferResponse == null) {
                        listProductOfferResponse = new ListProductOfferResponse();
                    }
                    return listProductOfferResponse;
                })
                .onErrorResume(throwable -> {
                    log.error("getProductInfo error: {}", throwable);
                    return Mono.just(new ListProductOfferResponse());
                });
    }
}
