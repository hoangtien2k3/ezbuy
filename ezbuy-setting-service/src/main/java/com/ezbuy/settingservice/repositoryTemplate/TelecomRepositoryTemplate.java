package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.ezbuy.settingmodel.model.Telecom;
import com.ezbuy.settingmodel.request.PageTelecomRequest;
import com.ezbuy.settingmodel.request.TelecomSearchingRequest;
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
