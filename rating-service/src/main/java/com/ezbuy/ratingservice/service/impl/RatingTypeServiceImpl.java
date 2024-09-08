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
package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.ratingmodel.model.RatingType;
import com.ezbuy.ratingservice.repository.RatingTypeRepository;
import com.ezbuy.ratingservice.service.RatingTypeService;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingTypeServiceImpl extends BaseServiceHandler implements RatingTypeService {

    private final RatingTypeRepository ratingTypeRepository;

    @Override
    public Mono<List<RatingType>> getAllActive() {
        return ratingTypeRepository.getAllActive().collectList();
    }
}
