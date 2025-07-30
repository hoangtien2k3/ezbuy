package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface UtilService {
    Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request);
}
