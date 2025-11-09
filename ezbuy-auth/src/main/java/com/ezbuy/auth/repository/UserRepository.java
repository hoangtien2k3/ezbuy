package com.ezbuy.auth.repository;

import com.ezbuy.authmodel.model.UserProfile;
import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<UserProfile, String>, UserRepositoryTemplate {
    @Query(value = "select now()")
    Mono<LocalDateTime> currentTimeDb();
}
