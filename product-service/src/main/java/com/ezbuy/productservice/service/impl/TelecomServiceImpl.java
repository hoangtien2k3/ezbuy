package com.ezbuy.productservice.service.impl;

import com.ezbuy.productservice.repository.TelecomRepository;
import com.ezbuy.productservice.service.TelecomService;
import com.ezbuy.sme.framework.constants.MessageConstant;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.settingmodel.model.Telecom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ezbuy.sme.framework.constants.MessageConstant.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelecomServiceImpl implements TelecomService {
    private final TelecomRepository telecomRepository;


    @Override
    public Mono<DataResponse<Telecom>> getByOriginId(String originId) {
        return this.telecomRepository.getByOriginId(originId).switchIfEmpty(Mono.just(new Telecom()))
                .flatMap(telecom -> Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), telecom)));
    }


    @Override
    public Mono<DataResponse<List<Telecom>>> getAllActive() {
        return this.telecomRepository.getAllActive().collectList().flatMap(lstService -> {
            return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), lstService));
        });
    }

    @Override
    public Mono<DataResponse<Telecom>> getByAlias(String telecomServiceAlias) {
        return this.telecomRepository.getByAlias(telecomServiceAlias).switchIfEmpty(Mono.just(new Telecom()))
                .flatMap(telecom -> Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), telecom)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getByLstAlias(List<String> lstTelecomServiceAlias) {
        return this.telecomRepository.getByLstAlias(lstTelecomServiceAlias).collectList()
                .flatMap(lstTelecom -> Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), lstTelecom)));
    }
}
