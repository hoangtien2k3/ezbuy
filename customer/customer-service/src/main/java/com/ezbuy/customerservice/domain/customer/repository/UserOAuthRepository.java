package com.ezbuy.customerservice.domain.customer.repository;

import com.ezbuy.customermodel.model.UserOAuth;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserOAuthRepository extends R2dbcRepository<UserOAuth, Long> {
    Mono<UserOAuth> findByAccessToken(String accessToken);
}
