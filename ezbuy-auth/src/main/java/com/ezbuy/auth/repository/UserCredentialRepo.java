package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.UserCredentialEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface UserCredentialRepo extends R2dbcRepository<UserCredentialEntity, String> {
    @Query(value = "select * from user_credential where username = :username and status = :status")
    Flux<UserCredentialEntity> getUserCredentialByUserName(String username, Integer status);
}
