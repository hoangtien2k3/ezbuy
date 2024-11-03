package com.ezbuy.productservice.client.impl;

import com.ezbuy.ordermodel.dto.request.PricingProductRequest;
import com.ezbuy.ordermodel.dto.response.GetOrderReportResponse;
import com.ezbuy.ordermodel.dto.response.PricingProductResponse;
import com.ezbuy.productservice.client.OrderClient;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class OrderClientImpl implements OrderClient {

    private final BaseRestClient baseRestClient;

    @Qualifier(value = "orderClient")
    private final WebClient orderClient;

    private final ObjectMapperUtil objectMapperUtil;

    @Override
    public Mono<PricingProductResponse> getPricingProduct(PricingProductRequest productRequest) {
        return baseRestClient
                .post(orderClient, "/order/pricing-product-internal", null, productRequest, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty()
                            || respOptional.get() == null
                            || respOptional.get().getData() == null) {
                        return null;
                    }

                    Map<String, Object> dataMap =
                            (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap);
                    return DataUtil.parseStringToObject(dataJson, PricingProductResponse.class);
                })
                .doOnError(err -> log.error("Exception when call order service: ", err))
                .onErrorResume(throwable -> Mono.error(
                        new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "order.service.error")));
    }

    @Override
    public Mono<Optional<GetOrderReportResponse>> getOrderReport(LocalDate dateReport) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("dateReport", String.valueOf(dateReport));
        return baseRestClient
                .get(orderClient, "/order/order-report", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.auth.error"));
                    }
                    String result = ObjectMapperUtil.convertObjectToJson(
                            dataResponseOptional.get().getData());
                    GetOrderReportResponse getOrderReportResponse =
                            objectMapperUtil.convertStringToObject(result, GetOrderReportResponse.class);
                    return Optional.ofNullable(getOrderReportResponse);
                })
                .switchIfEmpty(Mono.just(Optional.empty()))
                .onErrorResume(throwable -> {
                    log.error("order.service.error");
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "order.service.error"));
                });
    }
}
