/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.ratingservice.service;

import com.ezbuy.ratingmodel.dto.RatingServiceResponse;
import com.ezbuy.ratingmodel.dto.SearchRatingRequest;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
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
