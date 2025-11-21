package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentservice.client.SettingClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.util.DataUtil;

import java.util.Collections;
import java.util.List;

import com.ezbuy.paymentservice.model.dto.OptionSetValue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    private final WebClient settingClient;
    private final BaseRestClient baseRestClient;

    public SettingClientImpl(@Qualifier("settingClient") WebClient settingClient,
                             BaseRestClient baseRestClient) {
        this.settingClient = settingClient;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<List<OptionSetValue>> findByOptionSetCode(String optionSetCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("optionSetCode", DataUtil.safeTrim(optionSetCode));
        return baseRestClient
                .get(settingClient, "/v1/option-set/list", null, params, BaseRestClient.listOf(OptionSetValue.class))
                .map(opt -> opt.orElse(Collections.emptyList()));
    }
}
