package com.ezbuy.auth.repotemplate;

import com.ezbuy.auth.model.dto.request.ActionLogRequest;
import com.ezbuy.auth.model.entity.ActionLogEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionLogRepositoryTemplate {
    Flux<ActionLogEntity> search(ActionLogRequest request);

    Mono<Long> count(ActionLogRequest request);
}
