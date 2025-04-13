package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingmodel.model.RatingHistory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RatingHistoryService {

    Mono<RatingHistory> createRatingHistory(
            String ratingId,
            Long ratingBf,
            Long ratingAf,
            String contentBf,
            String contentAf,
            String approveBy,
            LocalDateTime approveAt,
            String state);
}
