package com.ezbuy.sme.sendnotificationservice.client.Impl;

import com.ezbuy.notimodel.dto.request.SendMessageRequest;
import com.ezbuy.notimodel.dto.response.SendMessageDTO;
import com.ezbuy.notimodel.dto.response.SmsBrandNameLoginResponse;
import com.ezbuy.ordermodel.dto.response.PricingProductResponse;
import com.ezbuy.sme.sendnotificationservice.client.SmsBrandNameClient;
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

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SmsBrandNameClientImpl implements SmsBrandNameClient {

    @Qualifier("smsBrandNameClient")
    private final WebClient smsBrandNameClient;
    private final BaseRestClient baseRestClient;

    @Override
    public Mono<SmsBrandNameLoginResponse> login(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", DataUtil.safeTrim(username));
        params.add("password", DataUtil.safeTrim(password));
        return baseRestClient.post(smsBrandNameClient,"/auth/ws/login", null, params, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty() || respOptional.get() == null || respOptional.get().getData() == null) {
                        return Mono.empty();
                    }
                    Map<String, Object> dataMap = (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap);
                    SmsBrandNameLoginResponse loginResponse = DataUtil.parseStringToObject(dataJson, SmsBrandNameLoginResponse.class);
                    return Mono.justOrEmpty(loginResponse);
                })
                .doOnError(err -> log.error("Exception when call smsBrandName website: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sms.brandname.service.error")));
    }

    @Override
    public Mono<SendMessageDTO> sendMessage(SendMessageRequest sendMessageReq) {
        return baseRestClient.post(smsBrandNameClient,"/ws/sent-mt", null, sendMessageReq, DataResponse.class)
                .map(responseSendMessage -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) responseSendMessage;
                    if (respOptional.isEmpty() || respOptional.get() == null || respOptional.get().getData() == null) {
                        log.warn("Send SMS failed: Response is empty");
                        return Mono.empty();
                    }
                    Map<String, Object> dataMap = (Map<String, Object>) respOptional.get().getData();
                    String dataJson = DataUtil.parseObjectToString(dataMap);
                    return DataUtil.parseStringToObject(dataJson, SendMessageDTO.class);
                })
                .doOnError(err -> log.error("Exception when send smsBrandName website: ", err))
                .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "sms.brandname.service.error")));
    }
}
