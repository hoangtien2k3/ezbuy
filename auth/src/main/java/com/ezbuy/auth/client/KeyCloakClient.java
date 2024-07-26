package com.ezbuy.auth.client;

import com.ezbuy.auth.dto.ClientResource;
import com.ezbuy.auth.dto.RoleDTO;
import com.ezbuy.auth.dto.request.CreateRoleUserKeycloakRequest;
import com.ezbuy.auth.dto.request.EmployeeCreateRequest;
import com.ezbuy.auth.dto.request.UpdateUserKeycloakRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.RolePolicyRepresentation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import com.ezbuy.auth.dto.request.LoginRequest;
import com.ezbuy.auth.dto.AccessToken;
import com.ezbuy.auth.dto.request.ProviderLogin;
import com.ezbuy.auth.dto.request.ClientLogin;
import com.ezbuy.auth.dto.request.LoginRequestSync;
import com.ezbuy.auth.dto.request.RefreshTokenRequest;
import com.ezbuy.auth.dto.request.LogoutRequest;
import com.ezbuy.auth.dto.response.Permission;

public interface KeyCloakClient {
    Mono<Optional<AccessToken>> getToken(LoginRequest loginRequest);

    Mono<Optional<AccessToken>> getToken(ProviderLogin providerLogin);

    Mono<Optional<AccessToken>> getToken(ClientLogin clientLogin);

    // ham lay token de dong bo
    Mono<Optional<AccessToken>> getToken(LoginRequestSync loginRequestSync);

    Mono<Optional<AccessToken>> refreshToken(RefreshTokenRequest refreshTokenRequest);

    Mono<Boolean> logout(LogoutRequest logoutRequest);

    Mono<List<Permission>> getPermissions(String audience, String token);

    Mono<List<ClientResource>> getClientResources(String clientId, String token);

    Mono<List<GroupPolicyRepresentation>> getGroupPolicies(String clientId, String token);

    Mono<List<RolePolicyRepresentation>> getRolePolicies(String clintId, String token);

    Mono<List<RoleDTO>> getRoleNameByUserIdAndClientId(String userId, String clientId, String token);

    //    Mono<List<Permission>> getPolicies(String audience, String token);
    Mono<String> createUser(EmployeeCreateRequest employeeCreateRequest, String password, String token);

    Mono<Boolean> createRoleUser(List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests, String userId, String clientId, String token);

    Mono<Boolean> updateUser(UpdateUserKeycloakRequest request, String token);

    Mono<Boolean> removeGroupToUser(String groupId, String userId, String token);

    Mono<Boolean> addGroupToUser(String groupId, String userId, String token);

    Mono<Boolean> removeRoleUser(List<CreateRoleUserKeycloakRequest> roleUserKeycloakRequests, String userId, String clientId, String token);

    Mono<UserRepresentation> getUser(String userId, String token);

    Mono<List<String>> getResourcesByClient(String clientId, String token);

    Mono<String> getGroupNameById(String groupId, String token);

    Mono<Boolean> addRoleForUserInClientId(String clientId, String token, RoleRepresentation roleRepresentation, String userId);
}
