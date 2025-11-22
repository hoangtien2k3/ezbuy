package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingservice.model.entity.RatingCount;
import reactor.core.publisher.Mono;

public interface RatingCountService {

    Mono<RatingCount> updateRatingCount(
            String ratingTypeCode, String targetId, Long newRatingPoint, Long oldRatingPoint);

    Mono<RatingCount> changeStatusRatingCount(
            String ratingTypeCode, String targetId, Long ratingPoint, Integer sumRateStatus);
}
