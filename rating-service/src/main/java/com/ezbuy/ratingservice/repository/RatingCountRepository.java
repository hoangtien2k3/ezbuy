package com.ezbuy.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingCount;
import java.util.List;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RatingCountRepository extends R2dbcRepository<RatingCount, String> {

    @Query("SELECT * FROM rating_count WHERE rating_type_code = 'service' AND target_id = ANY(:lstServiceAlias)")
    Flux<RatingCount> getRatingCountService(List<String> lstServiceAlias);

    @Query("SELECT * FROM rating_count")
    Flux<RatingCount> getAll();

    @Query("SELECT * FROM rating_count WHERE rating_type_code = :ratingTypeCode AND target_id = :targetId")
    Mono<RatingCount> getRatingCountByTypeAndTargetId(String ratingTypeCode, String targetId);
}
