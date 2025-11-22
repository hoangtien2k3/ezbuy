package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.TelecomDTO;
import com.ezbuy.settingservice.model.entity.Telecom;
import com.ezbuy.settingservice.model.dto.request.PageTelecomRequest;
import com.ezbuy.settingservice.model.dto.request.TelecomSearchingRequest;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TelecomRepositoryTemplate {

    Flux<TelecomDTO> getAll(List<String> ids, List<String> aliases, List<String> origins);

    Flux<String> getServiceTypes();

    Flux<Telecom> queryTelecomServices(TelecomSearchingRequest request);

    Mono<Long> countTelecomServices(TelecomSearchingRequest request);

    Flux<TelecomDTO> getAllByRequest(PageTelecomRequest request);

    Mono<Long> getTotalByRequest(PageTelecomRequest request);
}
