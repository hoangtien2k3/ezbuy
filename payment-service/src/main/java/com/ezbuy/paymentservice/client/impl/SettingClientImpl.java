package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentmodel.dto.TelecomDTO;
import com.ezbuy.paymentservice.client.SettingClient;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class SettingClientImpl implements SettingClient {

    @Qualifier("settingClient")
    private final WebClient settingClient;
    private final BaseRestClient baseRestClient;

    @Override
    public Mono<List<TelecomDTO>> getTelecomData(List<String> ids, List<String> aliases, List<String> origins) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        if (!DataUtil.isNullOrEmpty(ids)) {
            paramMap.set("ids", joinStringList(ids));
        }
        if (!DataUtil.isNullOrEmpty(aliases)) {
            paramMap.set("aliases", joinStringList(aliases));
        }
        if (!DataUtil.isNullOrEmpty(origins)) {
            paramMap.set("origins", joinStringList(origins));
        }
        return baseRestClient
                .get(settingClient, "/v1/telecom-services", null, paramMap, DataResponse.class)
                .flatMap(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.just(new ArrayList<>());
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.just(new ArrayList<>());
                    }

                    String dataJson = DataUtil.parseObjectToString(
                            dataResponseOptional.get().getData());

                    List<TelecomDTO> telecomList =
                            DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, new ArrayList<>());
                    return Mono.just(telecomList);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting telecomServiceData error: {}", throwable);
                    return new ArrayList<>();
                });
    }

    @Override
    public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("optionSetCode", DataUtil.safeTrim(optionSetCode));
        return baseRestClient
                .get(settingClient, "/v1/option-set/list", null, params, DataResponse.class)
                .flatMap(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.just(new ArrayList<>());
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.just(new ArrayList<>());
                    }

                    String dataJson = DataUtil.parseObjectToString(
                            dataResponseOptional.get().getData());

                    List<OptionSetValue> optionSetValues =
                            DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, new ArrayList<>());
                    return Mono.just(optionSetValues);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting optionSetValues error: {}", throwable);
                    return new ArrayList<>();
                });
    }

    private String joinStringList(List<String> inputList) {
        if (DataUtil.isNullOrEmpty(inputList)) {
            return "";
        }
        return inputList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
