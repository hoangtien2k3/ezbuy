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
import com.ezbuy.ratingmodel.dto.RatingServiceResponse;
import com.ezbuy.ratingmodel.dto.SearchRatingRequest;
import com.ezbuy.ratingservice.service.RatingService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.RatingService.PREFIX)
public class RatingServiceController {

    private final RatingService ratingService;

    /**
     * Ham lay danh sach danh gia theo dich vu
     *
     * @param serviceAlias
     * @return
     */
    @GetMapping()
    public Mono<DataResponse<RatingServiceResponse>> getRatingService(
            @RequestParam("service_alias") String serviceAlias) {
        return ratingService.getRatingService(serviceAlias);
    }

    @GetMapping(value = UrlPaths.Rating.GET_RATTING_SERVICE_PAGING)
    public Mono<DataResponse<RatingServiceResponse>> getRatingByServicePaging(
            @ModelAttribute SearchRatingRequest request) {
        return ratingService.getRatingServicePaging(request).map(rs -> new DataResponse("success", rs));
    }
}
