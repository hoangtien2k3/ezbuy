package com.ezbuy.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.customer.model.postgresql.UserOAuth;

import reactor.core.publisher.Mono;

public interface UserOAuthRepository extends R2dbcRepository<UserOAuth, Long> {
    Mono<UserOAuth> findByAccessToken(String accessToken);
}
