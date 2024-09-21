package com.ezbuy.authservice.repository;

import com.ezbuy.authmodel.model.UserCredential;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserCredentialRepo extends R2dbcRepository<UserCredential, String> {

    @Query(value = "select * from user_credential " + "where user_id = :userId and status = :status")
    Mono<UserCredential> getUserCredentialByUserId(String userId, Integer status);

    @Query(value = "select * from user_credential " + "where username = :username and status = :status")
    Flux<UserCredential> getUserCredentialByUserName(String username, Integer status);

    @Query("UPDATE user_credential " + "SET user_id = :userId, " + "    update_at = NOW(), "
            + "    update_by = :updateBy " + "WHERE id = :id")
    Mono<Integer> updateById(String id, String userId, String updateBy);
}
