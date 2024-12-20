package com.ezbuy.notificationsend.client.impl;

import com.ezbuy.notificationmodel.dto.request.SendMessageRequest;
import com.ezbuy.notificationmodel.dto.response.SendMessageDTO;
import com.ezbuy.notificationmodel.dto.response.SmsBrandNameLoginResponse;
import com.ezbuy.notificationsend.client.SmsBrandNameClient;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SmsBrandNameClientImpl implements SmsBrandNameClient {

    @Qualifier("smsBrandNameClient")
    private final WebClient smsBrandNameClient;
    private final BaseRestClient<DataResponse> baseRestClient;

    @Override
    public Mono<SmsBrandNameLoginResponse> login(String username, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("username", DataUtil.safeTrim(username));
        body.put("password", DataUtil.safeTrim(password));
        return baseRestClient.post(smsBrandNameClient,"/auth/ws/login", null, body, DataResponse.class)
                .flatMap(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get().getData() == null) {
                        return Mono.empty();
                    }
                    String dataJson = DataUtil.parseObjectToString(respOptional.get());
                    SmsBrandNameLoginResponse loginResponse = DataUtil.parseStringToObject(dataJson, SmsBrandNameLoginResponse.class);
                    return Mono.justOrEmpty(loginResponse);
                })
                .doOnError(err -> log.error("Exception when call smsBrandName website: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sms.brandname.service.error")));
    }

    @Override
    public Mono<SendMessageDTO> sendSMSBR(SendMessageRequest sendMessageReq, String auth) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("Authorization", "Bearer " + DataUtil.safeTrim(auth));
        return baseRestClient.post(smsBrandNameClient,"/ws/sent-mt", params, sendMessageReq, DataResponse.class)
                .flatMap(dataResponse -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) dataResponse;
                    if (respOptional.isEmpty() || respOptional.get() == null || respOptional.get().getData() == null) {
                        return Mono.empty();
                    }
                    String dataJson = DataUtil.parseObjectToString(respOptional.get());
                    SendMessageDTO loginResponse = DataUtil.parseStringToObject(dataJson, SendMessageDTO.class);
                    return Mono.justOrEmpty(loginResponse);
                })
                .doOnError(err -> log.error("Exception when send smsBrandName website: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sms.brandname.service.error")));
    }
}
