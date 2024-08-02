package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.dto.request.ActionLogRequest;
import com.ezbuy.auth.model.postgresql.ActionLog;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionLogRepositoryTemplate {
    /**
     * Function : Get action log list of user
     *
     * @param request : search params
     * @return
     */
    Flux<ActionLog> search(ActionLogRequest request);

    /**
     * Function : Get record total
     *
     * @param request : search params
     * @return
     */
    Mono<Long> count(ActionLogRequest request);
}
