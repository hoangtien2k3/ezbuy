package com.viettel.sme.ratingservice.repository;

import com.ezbuy.ratingmodel.model.RatingCount;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RatingCountRepository extends R2dbcRepository<RatingCount, String> {
    @Query(value = " select * from sme_rating.rating_count where rating_type_code = 'service' and target_id in (:lstServiceAlias) ")
    Flux<RatingCount> getRatingCountService(List<String> lstServiceAlias);

    @Query("SELECT * FROM rating_count ")
    Flux<RatingCount> getAll();

    @Query(value = " select * from sme_rating.rating_count where rating_type_code = :ratingTypeCode and target_id = :targetId ")
    Mono<RatingCount> getRatingCountByTypeAndTargetId(String ratingTypeCode, String targetId);
}
