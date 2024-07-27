package com.ezbuy.auth.repository;

import java.time.LocalDate;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ezbuy.auth.model.postgresql.ActionLog;

import reactor.core.publisher.Mono;

@Repository
public interface ActionLogRepository extends R2dbcRepository<ActionLog, String> {

    @Query("select count(*) " + "from action_log " + "where date(create_at) =:dateReport " + "  and type = :type")
    Mono<Integer> countLoginInOneDay(LocalDate dateReport, String type);
}
