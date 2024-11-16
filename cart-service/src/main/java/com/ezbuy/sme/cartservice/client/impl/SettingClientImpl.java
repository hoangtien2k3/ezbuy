package com.ezbuy.sme.cartservice.client.impl;

import com.ezbuy.cartmodel.dto.CartTelecomDTO;
import com.ezbuy.sme.cartservice.client.SettingClient;
import com.reactify.client.BaseRestClient;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class SettingClientImpl implements SettingClient {

    private final BaseRestClient baseRestClient;

    @Qualifier("setting")
    private final WebClient setting;

    @Override
    public Mono<List<CartTelecomDTO>> getTelecomService() {
        return baseRestClient
                .get(setting, "/telecom-services", null, null, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return new ArrayList<>();
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return new ArrayList<>();
                    }
                    List<CartTelecomDTO> cartTelecomDTOList = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> cartTelecomDTOList.add(
                            ObjectMapperFactory.getInstance().convertValue(x, CartTelecomDTO.class)));
                    return cartTelecomDTOList;
                })
                .onErrorResume(throwable -> Mono.just(new ArrayList()));
    }
}
