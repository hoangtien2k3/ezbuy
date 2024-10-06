package com.ezbuy.productservice.client.impl;

import com.ezbuy.productservice.client.SettingClient;
import com.ezbuy.settingmodel.dto.TelecomDTO;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.factory.ObjectMapperFactory;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class SettingClientImpl implements SettingClient {
    private final BaseRestClient baseRestClient;

    @Qualifier("settingClient")
    private final WebClient settingClient;

    @Override
    public Mono<List<TelecomDTO>> getTelecomServices(List<String> originalIds) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        if(originalIds != null) {
            for (String originId : originalIds) {
                req.add("origins", originId);
            }
        }
        return callSettings(req);
    }

    @Override
    public Mono<List<TelecomDTO>> getTelecomService(String aliases) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        if (!DataUtil.isNullOrEmpty(aliases)) {
            req.set("aliases", aliases);
        }
        return callSettings(req);
    }

    private Mono<List<TelecomDTO>> callSettings(MultiValueMap<String, String> req){
        return baseRestClient.get(settingClient, "/telecom-services", null, req, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                    }
                    List<TelecomDTO> telecomDTOList = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> telecomDTOList.add(ObjectMapperFactory.getInstance().convertValue(x, TelecomDTO.class)));
                    return telecomDTOList;
                })
                .onErrorResume(throwable -> {
                            log.error("call.api.setting.error");
                            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                        }
                );
    }

    @Override
    public Mono<List<TelecomDTO>> getTelecomServiceByOriginId(String originId) {
        MultiValueMap<String, String> req = new LinkedMultiValueMap<>();
        if (!DataUtil.isNullOrEmpty(originId)) {
            req.set("originId", originId);
        }
        return getTelecomService(req, "/telecom-services/filter");
    }

    private Mono<List<TelecomDTO>> getTelecomService(MultiValueMap<String, String> req, String url) {
        return baseRestClient.get(settingClient, url, null, req, DataResponse.class)
                .map(dataResponse -> {
                    if (DataUtil.isNullOrEmpty(dataResponse)) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                    }
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) dataResponse;
                    if (dataResponseOptional.isEmpty()) {
                        return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                    }
                    List<TelecomDTO> telecomDTOList = new ArrayList<>();
                    List list = (List) dataResponseOptional.get().getData();
                    list.forEach(x -> telecomDTOList.add(ObjectMapperFactory.getInstance().convertValue(x, TelecomDTO.class)));
                    return telecomDTOList;
                })
                .onErrorResume(throwable -> {
                            log.error("call.api.setting.error");
                            return Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("call.api.setting.error")));
                        }
                );
    }

    @Override
    public Mono<DataResponse> updateIsFilter(String telecomServiceId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originId", telecomServiceId);
        return baseRestClient.get(settingClient, "/telecom-services/init-filter/", null, params, DataResponse.class);
    }
}
