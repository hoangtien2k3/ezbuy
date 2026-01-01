package com.ezbuy.auth.client.impl;

import com.ezbuy.auth.constants.AuthConstants;
import com.ezbuy.auth.client.SettingClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.ezbuy.core.client.BaseRestClient.typeOf;

@Service
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    private final WebClient settingClient;
    private final BaseRestClient baseRestClient;

    public SettingClientImpl(@Qualifier("setting") WebClient settingClient,
                             BaseRestClient baseRestClient) {
        this.settingClient = settingClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<Optional<DataResponse<String>>> getAdminRole(String originId) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.set("originId", originId);
        return baseRestClient.get(
                settingClient,
                "/v1/telecom-services/admin-role",
                null,
                req,
                typeOf()
        );
    }

    @Override
    public Mono<Optional<DataResponse>> findOptionSetValueByCode(String optionSetCode, String optionSetValueCode) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        req.add(AuthConstants.OPTION_SET.OPTION_SET_CODE, optionSetCode);
        req.add(AuthConstants.OPTION_SET.OPTION_SET_VALUE_CODE, optionSetValueCode);
        return baseRestClient.get(settingClient, "/v1/option-set", null, req, DataResponse.class);
    }

    @Override
    public Mono<DataResponse<String>> findByCode(String code) {
        String uri = "/v1/setting/" + code;
        return baseRestClient
                .get(settingClient, uri, null, null, DataResponse.class)
                .map(rs -> {
                    DataResponse<?> response = (DataResponse<?>) rs.orElse(null);
                    String data = (response != null) ? (String) response.getData() : null;
                    return new DataResponse<>("success", data);
                });
    }
}
