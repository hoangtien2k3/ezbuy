package com.ezbuy.auth.service;

import com.ezbuy.auth.model.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface UtilService {
    Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request);
}
