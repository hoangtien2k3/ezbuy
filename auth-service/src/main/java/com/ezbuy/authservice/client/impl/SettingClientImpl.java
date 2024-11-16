package com.ezbuy.authservice.client.impl;

import com.ezbuy.authservice.client.SettingClient;
import com.ezbuy.settingmodel.dto.AreaDTO;
import com.ezbuy.settingmodel.model.OptionSetValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ezbuy.authmodel.constants.AuthConstants.OPTION_SET.OPTION_SET_CODE;
import static com.ezbuy.authmodel.constants.AuthConstants.OPTION_SET.OPTION_SET_VALUE_CODE;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {
    @Qualifier("settingClient")
    private final WebClient settingClient;
    private final BaseRestClient<DataResponse> baseRestClient;

    @Override
    public Mono<Optional<DataResponse>> getAdminRole(String originId) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.set("originId", originId);
        return baseRestClient.get(settingClient, "/v1/telecom-services/admin-role", null, req, DataResponse.class);
    }

    @Override
    public Mono<Optional<DataResponse>> getAdminRoleByAlias(String serviceAlias) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.set("serviceAlias", serviceAlias);
        return baseRestClient.get(settingClient, "/v1/telecom-services/admin-role/alias", null, req, DataResponse.class);
    }

    @Override
    public Mono<DataResponse<List<AreaDTO>>> getAreas(String parentCode) {
        return settingClient.get()
                .uri(builder -> builder.path("/v1/area").queryParam("parentCode", parentCode).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<DataResponse<List<AreaDTO>>>() {
                    @Override
                    public Type getType() {
                        return super.getType();
                    }
                })
                .doOnNext(res -> log.info("response for parent {}: {}", parentCode, res));
    }

    @Override
    public Mono<Optional<DataResponse>> getConfig(List<String> telecomServiceIds, List<String> originalIds, String syncType) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        if(telecomServiceIds != null && !telecomServiceIds.isEmpty()){
            for (String telecomServiceId: telecomServiceIds
            ) {
                req.add("telecomIds", telecomServiceId);
            }
        }
        if(originalIds != null && !originalIds.isEmpty()){
            for (String originalId: originalIds
            ) {
                req.add("originalIds", originalId);
            }
        }
        req.add("syncType", syncType);
        return baseRestClient.get(settingClient, "/v1/telecom-services/service-config", null, req, DataResponse.class);
    }

    @Override
    public Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add(OPTION_SET_CODE, optionSetCode);
        req.add(OPTION_SET_VALUE_CODE, optionSetValueCode);
        return baseRestClient.get(settingClient, "/v1/option-set", null, req, DataResponse.class);
    }

    @Override
    public Mono<Optional<DataResponse>> getLstAcronymByAliases(String code, List<String> aliases) {
        return null;
    }

    @Override
    public Mono<DataResponse<String>> findByCode(String code) {
        String uri = "/v1/setting/" + code;
        return baseRestClient.get(settingClient, uri, null, null, DataResponse.class)
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs.map(DataResponse::getData).orElse(null)));
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
                    Optional<DataResponse> dataResponseOptional = dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.just(new ArrayList<OptionSetValue>());
                    }
                    String dataJson = DataUtil.parseObjectToString(dataResponseOptional.get().getData());

                    List<OptionSetValue> optionSetValues = DataUtil.parseStringToObject(dataJson, new TypeReference<>() {}, result);
                    return Mono.just(optionSetValues);
                })
                .onErrorResume(throwable -> {
                    log.error("call ws setting optionSetValues error: {}", throwable);
                    List<OptionSetValue> result = new ArrayList<>();
                    return Mono.just(result);
                });
    }
}
