package com.ezbuy.productservice.client.impl;

import com.ezbuy.productmodel.dto.request.ValidateSubInsRequest;
import com.ezbuy.productmodel.dto.request.getListAreaInsRequest;
import com.ezbuy.productmodel.dto.response.ListAreaInsResponse;
import com.ezbuy.productmodel.dto.response.ValidateSubInsResponse;
import com.ezbuy.productservice.client.CaClient;
import com.ezbuy.productservice.client.properties.CaProperties;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class CaClientImpl implements CaClient {
    @Qualifier(value = "caClient")
    private final WebClient caClient;

    private final BaseRestClient baseRestClient;

    private final CaProperties caProperties;

    @Override
    public Mono<Optional<ValidateSubInsResponse>> validateSubIns(ValidateSubInsRequest request) {
        request.setUsername(caProperties.getUsername());
        request.setPassword(caProperties.getPassword());
        return baseRestClient
                .callPostBodyJson(caClient, "/validateSubIns", null, request, ValidateSubInsResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.ca.error")));
                    }
                    return dataResponse;
                })
                .onErrorResume(throwable -> {
                    log.error("call.api.ca.error");
                    ValidateSubInsResponse response = DataUtil.parseStringToObject(
                            ((BusinessException) throwable).getMessage(), ValidateSubInsResponse.class);
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR,
                            response != null
                                    ? Translator.toLocaleVi(response.getDescription())
                                    : Translator.toLocaleVi("call.api.ca.error")));
                });
    }

    @Override
    public Mono<Optional<ListAreaInsResponse>> getListAreaIns(getListAreaInsRequest request) {
        request.setUsername(caProperties.getUsername());
        request.setPassword(caProperties.getPassword());
        return baseRestClient
                .callPostBodyJson(caClient, "/getListAreaIns", null, request, ListAreaInsResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.ca.error")));
                    }
                    return dataResponse;
                })
                .onErrorResume(throwable -> {
                    log.error("call.api.ca.error");
                    ValidateSubInsResponse response = DataUtil.parseStringToObject(
                            ((BusinessException) throwable).getMessage(), ListAreaInsResponse.class);
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR,
                            response != null
                                    ? Translator.toLocaleVi(response.getDescription())
                                    : Translator.toLocaleVi("call.api.ca.error")));
                });
    }
}
