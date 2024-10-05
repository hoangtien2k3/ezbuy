package com.ezbuy.productservice.client.impl;

import com.ezbuy.sme.framework.client.BaseRestClient;
import com.ezbuy.sme.framework.constants.CommonErrorCode;
import com.ezbuy.sme.framework.exception.BusinessException;
import com.ezbuy.sme.framework.utils.DataUtil;
import com.ezbuy.sme.framework.utils.ObjectMapperUtil;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.productmodel.request.ValidateSubInsRequest;
import com.ezbuy.sme.productmodel.request.getListAreaInsRequest;
import com.ezbuy.sme.productmodel.response.ListAreaInsResponse;
import com.ezbuy.sme.productmodel.response.ValidateSubInsResponse;
import com.ezbuy.productservice.client.CaClient;
import com.ezbuy.productservice.client.properties.CaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
        return baseRestClient.callPostBodyJson(caClient, "/validateSubIns", null, request, ValidateSubInsResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.ca.error")));
                    }
                    return dataResponse;
                })
                .onErrorResume(throwable -> {
                            log.error("call.api.ca.error");
                            ValidateSubInsResponse response = DataUtil.parseStringToObject(((BusinessException) throwable).getMessage(), ValidateSubInsResponse.class);
                            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, response != null ? Translator.toLocaleVi(response.getDescription()) : Translator.toLocaleVi("call.api.ca.error")));
                        }
                );
    }

    @Override
    public Mono<Optional<ListAreaInsResponse>> getListAreaIns(getListAreaInsRequest request) {
        request.setUsername(caProperties.getUsername());
        request.setPassword(caProperties.getPassword());
        return baseRestClient.callPostBodyJson(caClient, "/getListAreaIns", null, request, ListAreaInsResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.ca.error")));
                    }
                    return dataResponse;
                })
                .onErrorResume(throwable -> {
                            log.error("call.api.ca.error");
                            ValidateSubInsResponse response = DataUtil.parseStringToObject(((BusinessException) throwable).getMessage(), ListAreaInsResponse.class);
                            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, response != null ? Translator.toLocaleVi(response.getDescription()) : Translator.toLocaleVi("call.api.ca.error")));
                        }
                );
    }
}
