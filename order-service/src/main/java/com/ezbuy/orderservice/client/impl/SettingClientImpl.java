package com.ezbuy.orderservice.client.impl;

import com.ezbuy.orderservice.client.SettingClient;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.dto.OptionSetValueDTO;
import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
public class SettingClientImpl implements SettingClient {

    private final BaseRestClient baseRestClient;

    private final WebClient settingClient;

    public SettingClientImpl(BaseRestClient baseRestClient, @Qualifier("settingClient") WebClient settingClient) {
        this.baseRestClient = baseRestClient;
        this.settingClient = settingClient;
    }

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

    private String joinStringList(List<String> inputList) {
        if (DataUtil.isNullOrEmpty(inputList)) {
            return "";
        }
        return inputList.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public Mono<List<OptionSetValueDTO>> getConfDataPolicy(String code) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.set("code", code);
        return baseRestClient
                .get(settingClient, "/v1/option-set-value/option-set", null, paramMap, DataResponse.class)
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

                    List<OptionSetValueDTO> optionSetValueList =
                            DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, new ArrayList<>());
                    return Mono.just(optionSetValueList);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting getConfDataPolicy error: {}", throwable);
                    return new ArrayList<>();
                });
    }

    @Override
    public Mono<List<Telecom>> getAllTelecomService() {
        return baseRestClient
                .get(settingClient, "/v1/telecom-services/all", null, null, DataResponse.class)
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

                    List<Telecom> telecomList =
                            DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, new ArrayList<>());
                    return Mono.just(telecomList);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting get all telecomService error: {}", throwable);
                    return new ArrayList<>();
                });
    }

    @Override
    public Mono<List<OptionSetValue>> getAllActiveOptionSetValueByOptionSetCode(String code) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.set("code", code);
        return baseRestClient
                .get(settingClient, "/v1/option-set-value/code", null, paramMap, DataResponse.class)
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

                    List<OptionSetValue> optionSetValueList = DataUtil.parseStringToObject(
                            dataJson, new TypeReference<List<OptionSetValue>>() {}, new ArrayList<>());
                    return Mono.just(optionSetValueList);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting getAllOptionSetValueByOptionSetCode error: {}", throwable);
                    return new ArrayList<>();
                });
    }

    @Override
    public Mono<AreaDTO> getAreaName(String province, String district, String precinct) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("province", province);
        params.add("district", district);
        params.add("precinct", precinct);
        return baseRestClient
                .get(settingClient, "v1/area/get-area-name", null, params, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.setting.error"));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.setting.error"));
                    }
                    String dataJson = DataUtil.parseObjectToString(
                            dataResponseOptional.get().getData());
                    try {
                        return ObjectMapperFactory.getInstance().readValue(dataJson, AreaDTO.class);
                    } catch (Exception ex) {
                        log.error("convert json error: ", ex.getMessage());
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("setting.service.error")));
                    }
                })
                .onErrorResume(throwable -> {
                    log.error("call.api.setting.error");
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "call.api.setting.error"));
                });
    }
}
