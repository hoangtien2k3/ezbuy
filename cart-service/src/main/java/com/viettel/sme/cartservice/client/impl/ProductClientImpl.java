package com.viettel.sme.cartservice.client.impl;

import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.response.ListProductOfferResponse;
import com.viettel.sme.cartservice.client.ProductClient;
import com.viettel.sme.cartservice.client.properties.ProductProperties;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("webClientFactory")
public class ProductClientImpl implements ProductClient {
    private final BaseRestClient baseRestClient;

    @Qualifier("product")
    private final WebClient product;

    private final ProductProperties productProperties;

    @Override
    public Mono<ListProductOfferResponse> getProductInfo(List<String> templateIds) {
        FilterProductTemplateDTO filterProductTemplateDTO = new FilterProductTemplateDTO();
        filterProductTemplateDTO.setListId(templateIds);
        return baseRestClient.post(product, "/v1/filter-product-template", null, filterProductTemplateDTO, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("server.error")));
                    }

                    String jsonValue = DataUtil.parseObjectToString(respOptional.get().getData());
                    ListProductOfferResponse listProductOfferResponse = DataUtil.parseStringToObject(jsonValue, ListProductOfferResponse.class);
                    if (listProductOfferResponse == null) {
                        listProductOfferResponse = new ListProductOfferResponse();
                    }
                    return listProductOfferResponse;
                }).onErrorResume(throwable -> {
                    log.error("getProductInfo error: {}", throwable);
                    return Mono.just(new ListProductOfferResponse());
                });
    }
}
