package com.ezbuy.auth.infrastructure.repository;

import com.ezbuy.auth.application.dto.UserProfileDTO;
import com.ezbuy.auth.application.dto.request.QueryUserRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryTemplate {
    Flux<UserProfileDTO> queryUserProfile(QueryUserRequest request);

    Mono<Long> countUserProfile(QueryUserRequest request);
}
