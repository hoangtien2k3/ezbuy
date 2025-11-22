package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.UserProfileEntity;
import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserProfileEntity, String>, UserRepositoryTemplate {
    @Query(value = "select now()")
    Mono<LocalDateTime> currentTimeDb();
}
