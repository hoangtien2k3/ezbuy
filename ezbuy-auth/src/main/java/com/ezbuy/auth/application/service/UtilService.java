package com.ezbuy.auth.application.service;

import com.ezbuy.auth.application.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface UtilService {
    Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request);
}
