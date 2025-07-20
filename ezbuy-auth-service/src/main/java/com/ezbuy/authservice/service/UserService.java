package com.ezbuy.authservice.service;

import com.ezbuy.authmodel.dto.UserProfileDTO;
import com.ezbuy.authmodel.dto.request.QueryUserRequest;
import com.ezbuy.authmodel.dto.request.UpdateUserRequest;
import com.ezbuy.authmodel.dto.response.QueryUserResponse;
import com.ezbuy.authmodel.dto.response.UserContact;
import com.ezbuy.authmodel.model.UserProfile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Optional<UserProfile>> getUserProfile();

    Mono<UserProfile> update(UpdateUserRequest u);

    Flux<UserContact> getUserContacts(List<UUID> userIds);

    Mono<Optional<UserProfile>> getUserById(String id);

    Mono<QueryUserResponse> queryUserProfile(QueryUserRequest request);

    Mono<Resource> exportUser(QueryUserRequest request);

    Mono<UserProfileDTO> getUserProfile(String id);

    Mono<UserRepresentation> getEmailsByKeycloakUsername(String username);
}
