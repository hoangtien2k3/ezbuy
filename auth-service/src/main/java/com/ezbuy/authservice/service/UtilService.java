/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import io.hoangtien2k3.commons.model.response.DataResponse;
import reactor.core.publisher.Mono;

/**
 * Interface for utility services in the application.
 */
public interface UtilService {

    /**
     * Adds the admin role of EzBuy to old users.
     * <p>
     * This method is responsible for migrating the admin role to old users based on
     * the provided request parameters.
     *
     * @param request
     *            the request object containing the details required for the
     *            migration.
     *            <p>
     *            - roleId: the ID of the role in Keycloak.
     *            <p>
     *            - roleName: the name or code of the role in Keycloak.
     *            <p>
     *            - clientId: the client ID of the service.
     *            <p>
     *            - policyId: the policy ID of the service.
     *            <p>
     *            - offset: the offset for migration.
     *            <p>
     *            - limit: the limit for each offset.
     *            <p>
     *            - username: the username of the user.
     * @return a Mono emitting a DataResponse object containing the result of the
     *         operation.
     */
    Mono<DataResponse<Object>> jobAddRoleAdminForOldUser(JobAddRoleAdminForOldUserRequest request);
}
