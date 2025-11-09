package com.ezbuy.auth.application.service;

import com.ezbuy.auth.application.dto.UserProfileDTO;
import com.ezbuy.auth.application.dto.request.QueryUserRequest;
import com.ezbuy.auth.application.dto.request.UpdateUserRequest;
import com.ezbuy.auth.application.dto.response.QueryUserResponse;
import com.ezbuy.auth.application.dto.response.UserContact;
import com.ezbuy.auth.domain.model.entity.UserProfileEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Optional<UserProfileEntity>> getUserProfile();

    Mono<UserProfileEntity> update(UpdateUserRequest u);

    Flux<UserContact> getUserContacts(List<UUID> userIds);

    Mono<Optional<UserProfileEntity>> getUserById(String id);

    Mono<QueryUserResponse> queryUserProfile(QueryUserRequest request);

    Mono<Resource> exportUser(QueryUserRequest request);

    Mono<UserProfileDTO> getUserProfile(String id);

    Mono<UserRepresentation> getEmailsByKeycloakUsername(String username);
}
