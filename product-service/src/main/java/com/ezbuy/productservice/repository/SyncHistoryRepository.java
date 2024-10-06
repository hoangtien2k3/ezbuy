package com.ezbuy.productservice.repository;

import com.ezbuy.productmodel.model.SyncHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SyncHistoryRepository extends R2dbcRepository<SyncHistory, UUID> {
    @Query("select nextval(customerSeq)")
    Mono<Long> getSequence();

    @Query("select * from sync_history where id = :id")
    Mono<SyncHistory> getSyncHistoryById(String id);

    @Query("select CURRENT_TIMESTAMP")
    Mono<LocalDateTime> getSysDate();

    @Query(value = "select * from sync_history " +
            " where (:startTime is null or create_at >= :startTime)" +
            " and (:endTime is null or create_at < :endTime)" +
            " and state = :state" +
            " and status = 1" +
            " and (:retry is null or retry < :retry) " +
            " order by create_at" +
            " limit :limit offset :offset")
    Flux<SyncHistory> findAllSyncHistoryByStateAndTime(String state, Integer retry, LocalDateTime startTime, LocalDateTime endTime, int limit, long offset);

    @Query("update" +
            " sync_history " +
            "set " +
            " state = :state, " +
            " update_by = :updateBy, " +
            " update_at = CURRENT_TIMESTAMP() " +
            "where " +
            " id in (:ids) ")
    Mono<SyncHistory> updateStateSyncHistory(List<String> ids,
                                             String state,
                                             String updateBy);
}
