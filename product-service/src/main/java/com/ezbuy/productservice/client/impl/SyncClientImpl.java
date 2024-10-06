package com.ezbuy.productservice.client.impl;

import com.ezbuy.productmodel.request.CallApiSyncProductRequest;
import com.ezbuy.productmodel.response.GetTransSyncResponse;
import com.ezbuy.productservice.client.SyncClient;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.factory.ObjectMapperFactory;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SyncClientImpl implements SyncClient {
    @Qualifier("syncClient")
    private final WebClient syncClient;

    private final BaseRestClient baseRestClient;

    @Override
    public Mono<DataResponse> callApiSyncProduct(CallApiSyncProductRequest request) {
        log.info("Call job syncCustomer");
        return baseRestClient.callPostBodyJsonForLocalDateTime(syncClient, "/sync/push", null, request, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.customer.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.customer.error"));
                    }
                    return new DataResponse<>(CommonErrorCode.SUCCESS, dataResponseOptional.get());
                })
                .onErrorReturn(new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR))
                .onErrorResume(e -> {
                            log.error("call.api.customer.error");
                            return new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.customer.error"));
                        }
                );
    }

    @Override
    public Mono<String> getProductSyncTransId(String userName) {
        log.info("Get Customer sync trans id");
        CallApiSyncProductRequest request = CallApiSyncProductRequest.builder().userName(userName).build();
        return baseRestClient.callPostBodyJson(syncClient, "/sync/trans", null, request, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.customer.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.customer.error"));
                    }
                    GetTransSyncResponse transSyncResponse = ObjectMapperFactory.getInstance().convertValue(dataResponseOptional.get().getData(), GetTransSyncResponse.class);
                    return transSyncResponse.getTransactionId();
                })
                .onErrorReturn("")
                .onErrorResume(e -> {
                            log.error("call.api.customer.error");
                            return "";
                        }
                );
    }
}
