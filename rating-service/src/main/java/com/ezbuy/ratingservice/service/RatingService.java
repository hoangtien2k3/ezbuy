package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingmodel.dto.RatingServiceResponse;
import com.ezbuy.ratingmodel.dto.SearchRatingRequest;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface RatingService {
    Mono<DataResponse<RatingServiceResponse>> getRatingService(String serviceAlias);

    Mono<List<RatingCount>> getAllRatingActive();

    Mono<List<RatingCount>> getRatingByAlias(List<String> alias);

    /**
     * Tao moi danh gia
     *
     * @param request
     * @return
     */
    Mono<DataResponse<Rating>> createRating(RatingRequest request);

    /**
     * cap nhat danh gia
     *
     * @param id
     * @param request
     * @return
     */
    Mono<DataResponse<Rating>> editRating(String id, RatingRequest request);

    /**
     * tim kiem danh gia
     *
     * @param request
     * @return
     */
    Mono<SearchRatingResponse> findRating(FindRatingRequest request);

    // Mono<List<Rating>> getAllRatingActive();

    Mono<DataResponse<RatingServiceResponse>> getRatingServicePaging(SearchRatingRequest request);
}
