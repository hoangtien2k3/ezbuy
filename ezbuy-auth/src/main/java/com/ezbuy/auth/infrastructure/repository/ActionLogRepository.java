package com.ezbuy.auth.infrastructure.repository;

import com.ezbuy.auth.domain.model.entity.ActionLogEntity;
import java.time.LocalDate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ActionLogRepository extends R2dbcRepository<ActionLogEntity, String> {

    /**
     * Counts the number of login actions in a single day.
     *
     * @param dateReport
     *            the date for which to count login actions
     * @param type
     *            the type of action to count
     * @return a Mono emitting the count of login actions for the specified date and
     *         type
     */
    @Query("select count(*) from action_log where create_at::date = :dateReport and type = :type")
    Mono<Integer> countLoginInOneDay(LocalDate dateReport, String type);
}
