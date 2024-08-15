package com.viettel.sme.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingHistory;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RatingHistoryRepository extends R2dbcRepository<RatingHistory, String> {

    @Query("select current_timestamp")
    Mono<LocalDateTime> getSysDate();
}
