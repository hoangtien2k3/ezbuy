package com.ezbuy.settingservice.repository;

import com.ezbuy.settingmodel.model.OptionSet;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface OptionSetRepository extends R2dbcRepository<OptionSet, String> {
    @Query(value = "select * from option_set where id = :id")
    Mono<OptionSet> getById(String id);

    @Query(value = "update option_set set code = :code, description = :description, status = :status, update_by = :user, update_at = CURRENT_TIMESTAMP() where id = :id")
    Mono<OptionSet> updateOptionSet(String id, String code, String description, Integer status, String user);

    @Query("select * from option_set where code = :code")
    Mono<OptionSet> findByCode(String code);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();
}
