package com.ezbuy.notificationsend.client.impl;

import com.ezbuy.notificationsend.client.SettingClient;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.ArrayList;
import java.util.List;
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
    public Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add("OPTION_SET_CODE", optionSetCode);
        req.add("OPTION_SET_VALUE_CODE", optionSetValueCode);
        return baseRestClient.get(settingClient, "/v1/option-set", null, req, DataResponse.class);
    }

    @Override
    public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("optionSetCode", DataUtil.safeTrim(optionSetCode));
        return baseRestClient.get(settingClient, "/v1/option-set/list", null, params, DataResponse.class)
                .flatMap(dataResponse -> {
                    List<OptionSetValue> result = new ArrayList<>();
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.just(result);
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.just(new ArrayList<OptionSetValue>());
                    }

                    String dataJson = DataUtil.parseObjectToString(dataResponseOptional.get().getData());
                    List<OptionSetValue> optionSetValues = DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, result);
                    return Mono.just(optionSetValues);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting optionSetValues error: ", throwable);
                    List<OptionSetValue> result = new ArrayList<>();
                    return Mono.just(result);
                });
    }

}
