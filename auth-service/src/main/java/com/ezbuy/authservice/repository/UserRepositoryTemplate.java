package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.dto.UserProfileDTO;
import com.ezbuy.authmodel.dto.request.QueryUserRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryTemplate {
    Flux<UserProfileDTO> queryUserProfile(QueryUserRequest request);

    Mono<Long> countUserProfile(QueryUserRequest request);
}
