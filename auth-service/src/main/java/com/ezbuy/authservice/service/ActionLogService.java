package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.ActionLogRequest;
import com.ezbuy.authmodel.dto.response.SearchActionLogResponse;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Mono;

public interface ActionLogService {

    /**
     * Function : Get action log list of user
     * @param request : search params
     * @return
     */
    Mono<SearchActionLogResponse> search(ActionLogRequest request);

    /**
     * Function : Get excel file
     * @param request : search params
     * @return
     */
    Mono<Resource> exportUser(ActionLogRequest request);
}
