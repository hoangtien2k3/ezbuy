package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.UserCredentialEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserCredentialRepository extends R2dbcRepository<UserCredentialEntity, String> {
    @Query(value = "select * from user_credential where user_id = :userId")
    Mono<UserCredentialEntity> findByUserId(String userId);

    @Query(value = "select username, pwd_changed from user_credential where username = :username")
    Mono<UserCredentialEntity> findByUsername(String username);
}
