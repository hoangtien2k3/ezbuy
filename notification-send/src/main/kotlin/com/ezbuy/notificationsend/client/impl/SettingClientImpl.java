package com.ezbuy.notificationsend.client.impl;

import com.ezbuy.notificationsend.client.SettingClient;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.reactify.client.BaseRestClient;
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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    @Qualifier("settingClient")
    private final WebClient settingClient;
    private final BaseRestClient<DataResponse> baseRestClient;

    @Override
    public Mono<OptionSetValue> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("optionSetCode", optionSetCode);
        req.add("optionSetValueCode", optionSetValueCode);
        return baseRestClient.get(settingClient, "/v1/option-set", null, req, DataResponse.class)
                .flatMap(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.just(new OptionSetValue());
                    }
                    if (((Optional<DataResponse>) dataResponse).isEmpty()) {
                        return Mono.just(new OptionSetValue());
                    }
                    String dataJson = DataUtil.parseObjectToString(((Optional<DataResponse>) dataResponse).get().getData());
                    OptionSetValue optionSetValues = DataUtil.parseStringToObject(dataJson, OptionSetValue.class);
                    return Mono.just(optionSetValues != null ? optionSetValues : new OptionSetValue());
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting optionSetValues error: ", throwable);
                    return Mono.just(new OptionSetValue());
                });
    }

}
