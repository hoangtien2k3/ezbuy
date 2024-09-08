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
import com.ezbuy.ratingmodel.model.RatingType;
import com.ezbuy.ratingservice.service.RatingTypeService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.RatingType.PREFIX)
@RequiredArgsConstructor
@CrossOrigin
public class RatingTypeController {
    private final RatingTypeService ratingTypeService;

    @GetMapping(value = UrlPaths.RatingType.GET_ALL_ACTIVE)
    public Mono<DataResponse<List<RatingType>>> getAllRatingActive() {
        return ratingTypeService.getAllActive().map(rs -> new DataResponse<>("success", rs));
    }
}
