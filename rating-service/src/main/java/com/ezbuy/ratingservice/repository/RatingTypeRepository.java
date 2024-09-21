package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingType;
import java.time.LocalDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingTypeRepository extends R2dbcRepository<RatingType, String> {
    @Query("select * from sme_rating.rating_type where status = 1 ORDER BY name")
    Flux<RatingType> getAllActive();

    @Query("select current_timestamp")
    Mono<LocalDateTime> getSysDate();
}
