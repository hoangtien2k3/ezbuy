package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.dto.UserProfileDTO;
import com.ezbuy.auth.model.request.QueryUserRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryTemplate {

    Flux<UserProfileDTO> queryUserProfile(QueryUserRequest request);

    Mono<Long> countUserProfile(QueryUserRequest request);
}
