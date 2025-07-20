package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.UserCredential;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserCredentialRepository extends R2dbcRepository<UserCredential, String> {
    @Query(value = "select * from user_credential where user_id = :userId")
    Mono<UserCredential> findByUserId(String userId);

    @Query(value = "select username, pwd_changed from user_credential where username = :username")
    Mono<UserCredential> findByUsername(String username);
}
