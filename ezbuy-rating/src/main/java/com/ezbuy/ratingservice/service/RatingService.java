package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingservice.model.dto.RatingServiceResponse;
import com.ezbuy.ratingservice.model.dto.SearchRatingRequest;
import com.ezbuy.ratingservice.model.entity.Rating;
import com.ezbuy.ratingservice.model.entity.RatingCount;
import com.ezbuy.ratingservice.model.dto.request.FindRatingRequest;
import com.ezbuy.ratingservice.model.dto.request.RatingRequest;
import com.ezbuy.ratingservice.model.dto.response.SearchRatingResponse;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<DataResponse<RatingServiceResponse>> getRatingService(String serviceAlias);

    Mono<List<RatingCount>> getAllRatingActive();

    Mono<List<RatingCount>> getRatingByAlias(List<String> alias);

    Mono<DataResponse<Rating>> createRating(RatingRequest request);

    Mono<DataResponse<Rating>> editRating(String id, RatingRequest request);

    Mono<SearchRatingResponse> findRating(FindRatingRequest request);

    // Mono<List<Rating>> getAllRatingActive();

    Mono<DataResponse<RatingServiceResponse>> getRatingServicePaging(SearchRatingRequest request);
}
