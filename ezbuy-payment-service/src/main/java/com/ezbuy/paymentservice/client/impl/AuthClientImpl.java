package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentservice.client.AuthClient;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class AuthClientImpl implements AuthClient {

    @Qualifier("authClient")
    private final WebClient authClient;

    private final BaseRestClient baseRestClient;

    @Override
    public Mono<List<String>> getTrustedIdNoOrganization(String organizationId) {
        return SecurityUtils.getCurrentUser()
                .map(currentUser -> {
                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("organizationId", organizationId);
                    return params;
                })
                .flatMap(params -> baseRestClient.get(
                        authClient, "/identify/trusted-idNo-organization", null, params, DataResponse.class))
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("auth.service.error")));
                    }
                    List<String> lstIdNo = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(
                            x -> lstIdNo.add(ObjectMapperFactory.getInstance().convertValue(x, String.class)));
                    return lstIdNo;
                })
                .doOnError(err ->
                        log.error("Exception when call auth service api/identify/trusted-idNo-organization: ", err))
                .onErrorResume(throwable ->
                        Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "auth.service.error")));
    }
}
