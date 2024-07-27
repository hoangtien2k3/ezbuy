package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.postgresql.UserCredential;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserCredentialRep extends R2dbcRepository<UserCredential, UUID> {

    @Query(value = "select * from user_credential " +
            "where user_id = :userId and status = :status")
    Mono<UserCredential> getUserCredentialByUserId(String userId, Integer status);

    @Query(value = "select * from user_credential " +
            "where username = :username and status = :status")
    Flux<UserCredential> getUserCredentialByUserName(String username, Integer status);

    @Query(value = "update " +
            "    user_credential " +
            "set user_id   = :userId, " +
            "    update_at = now(), " +
            "    update_by =:updateBy " +
            "where id = :id")
    Mono<Integer> updateById(String id, String userId, String updateBy);
}
