package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingmodel.dto.RatingServiceResponse;
import com.ezbuy.ratingmodel.dto.SearchRatingRequest;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import com.reactify.model.response.DataResponse;
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
