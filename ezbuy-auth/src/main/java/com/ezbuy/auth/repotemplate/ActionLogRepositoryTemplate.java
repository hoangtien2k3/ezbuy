package com.ezbuy.auth.repotemplate;

import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authmodel.model.ActionLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionLogRepositoryTemplate {
    Flux<ActionLog> search(ActionLogRequest request);

    Mono<Long> count(ActionLogRequest request);
}
