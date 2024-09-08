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
package com.ezbuy.ratingservice.controller;

import com.ezbuy.ratingmodel.constants.UrlPaths;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import com.ezbuy.ratingservice.service.RatingService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Rating.PREFIX)
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping(value = UrlPaths.Rating.GET_ALL_RATTING_ACTIVE)
    public Mono<DataResponse<List<RatingCount>>> getAllRatingActive() {
        return ratingService.getAllRatingActive().map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping(value = UrlPaths.Rating.GET_RATTING_SERVICE)
    public Mono<DataResponse<List<RatingCount>>> getRatingByServiceId(@RequestBody List<String> alias) {
        return ratingService.getRatingByAlias(alias).map(rs -> new DataResponse<>("success", rs));
    }

    @PostMapping
    public Mono<DataResponse<Rating>> createRating(@RequestBody RatingRequest request) {
        return ratingService.createRating(request);
    }

    @PutMapping(value = UrlPaths.Rating.UPDATE)
    public Mono<DataResponse<Rating>> editRating(@PathVariable String id, @Valid @RequestBody RatingRequest request) {
        return ratingService.editRating(id, request);
    }

    @GetMapping(value = UrlPaths.Rating.SEARCH)
    public Mono<SearchRatingResponse> findRating(FindRatingRequest request) {
        return ratingService.findRating(request);
    }

    @GetMapping(UrlPaths.Rating.ALL)
    public Mono<DataResponse<List<Rating>>> getAllServiceGroupActive() {
        return ratingService.getAllRatingActive().map(rs -> new DataResponse("success", rs));
    }
}
