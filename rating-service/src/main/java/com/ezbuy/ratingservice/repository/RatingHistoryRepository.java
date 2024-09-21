package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingHistory;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RatingHistoryRepository extends R2dbcRepository<RatingHistory, String> {

    @Query("select current_timestamp")
    Mono<LocalDateTime> getSysDate();
}
