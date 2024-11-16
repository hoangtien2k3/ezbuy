package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productservice.repository.TelecomRepository;
import com.ezbuy.productservice.service.TelecomService;
import com.reactify.constants.MessageConstant;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelecomServiceImpl implements TelecomService {
    private final TelecomRepository telecomRepository;

    @Override
    public Mono<DataResponse<Telecom>> getByOriginId(String originId) {
        return this.telecomRepository
                .getByOriginId(originId)
                .switchIfEmpty(Mono.just(new Telecom()))
                .flatMap(telecom ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), telecom)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getAllActive() {
        return this.telecomRepository
                .getAllActive()
                .collectList()
                .flatMap(lstService -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), lstService)));
    }

    @Override
    public Mono<DataResponse<Telecom>> getByAlias(String telecomServiceAlias) {
        return this.telecomRepository
                .getByAlias(telecomServiceAlias)
                .switchIfEmpty(Mono.just(new Telecom()))
                .flatMap(telecom ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), telecom)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getByLstAlias(List<String> lstTelecomServiceAlias) {
        return this.telecomRepository
                .getByLstAlias(lstTelecomServiceAlias)
                .collectList()
                .flatMap(lstTelecom ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), lstTelecom)));
    }
}
