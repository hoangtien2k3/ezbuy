package com.ezbuy.auth.service;

import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface UtilService {
    Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request);
}
