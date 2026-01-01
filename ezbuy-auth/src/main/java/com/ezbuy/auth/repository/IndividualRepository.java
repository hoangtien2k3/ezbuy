package com.ezbuy.auth.repository;

import com.ezbuy.auth.model.entity.IndividualEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface IndividualRepository extends R2dbcRepository<IndividualEntity, String> {

    @NotNull
    @Query("""
            SELECT * FROM individual
            WHERE status IN (0, 1)
              AND id = :id
            """)
    Mono<IndividualEntity> findById(@NotNull String id);
}
