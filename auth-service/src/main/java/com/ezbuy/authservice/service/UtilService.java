package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.request.JobAddRoleAdminForOldUserRequest;
import io.hoangtien2k3.reactify.model.response.DataResponse;
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
