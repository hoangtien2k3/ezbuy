package com.ezbuy.authservice.repotemplate;

import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authmodel.model.ActionLog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActionLogRepositoryTemplate {
    /**
     * Function : Get action log list of user
     *
     * @param request
     *            : search params
     * @return
     */
    Flux<ActionLog> search(ActionLogRequest request);

    /**
     * Function : Get record total
     *
     * @param request
     *            : search params
     * @return
     */
    Mono<Long> count(ActionLogRequest request);
}
