package com.ezbuy.auth.infrastructure.repotemplate;

import com.ezbuy.auth.application.dto.request.ActionLogRequest;
import com.ezbuy.auth.domain.model.entity.ActionLogEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionLogRepositoryTemplate {
    Flux<ActionLogEntity> search(ActionLogRequest request);

    Mono<Long> count(ActionLogRequest request);
}
