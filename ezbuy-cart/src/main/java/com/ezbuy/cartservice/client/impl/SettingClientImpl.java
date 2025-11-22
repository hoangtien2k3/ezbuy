package com.ezbuy.cartservice.client.impl;

import com.ezbuy.cartservice.domain.dto.CartTelecomDTO;
import com.ezbuy.cartservice.client.SettingClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.factory.ObjectMapperFactory;
import com.ezbuy.core.model.response.DataResponse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    private final WebClient setting;
    private final BaseRestClient baseRestClient;

    public SettingClientImpl(@Qualifier("settingServiceClient") WebClient setting,
                             BaseRestClient baseRestClient) {
        this.setting = setting;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<List<CartTelecomDTO>> getTelecomService() {
        return baseRestClient
                .get(setting, "/telecom-services", null, null, DataResponse.class)
                .map(dataResponseOptional -> {
                    if (dataResponseOptional.isEmpty()) {
                        return List.<CartTelecomDTO>of();
                    }
                    List<CartTelecomDTO> cartTelecomDTOList = new ArrayList<>();
                    List<?> list = (List<?>) dataResponseOptional.get().getData();
                    list.forEach(x -> cartTelecomDTOList.add(ObjectMapperFactory.getInstance().convertValue(x, CartTelecomDTO.class)));
                    return cartTelecomDTOList;
                })
                .onErrorResume(throwable -> Mono.just(List.of()));
    }
}
