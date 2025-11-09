package com.ezbuy.cartservice.client.impl;

import com.ezbuy.cartservice.client.ProductClient;
import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.dto.response.ListProductOfferResponse;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.Translator;
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
