package com.viettel.sme.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingType;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RatingTypeRepository extends R2dbcRepository<RatingType, String> {
    @Query("select * from sme_rating.rating_type where status = 1 ORDER BY name")
    Flux<RatingType> getAllActive();

    @Query("select current_timestamp")
    Mono<LocalDateTime> getSysDate();
}
