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

import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingHistory;
import com.ezbuy.ratingservice.repository.RatingHistoryRepository;
import com.ezbuy.ratingservice.service.RatingHistoryService;
import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import io.hoangtien2k3.commons.utils.SecurityUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingHistoryServiceImpl extends BaseServiceHandler implements RatingHistoryService {

    private final RatingHistoryRepository ratingHistoryRepository;

    @Override
    public Mono<RatingHistory> createRatingHistory(
            String ratingId,
            Long ratingBf,
            Long ratingAf,
            String contentBf,
            String contentAf,
            String approveBy,
            LocalDateTime approveAt,
            Rating.RatingState state) {
        var getSysDate = ratingHistoryRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT2();
                    RatingHistory ratingHistory = RatingHistory.builder()
                            .id(UUID.randomUUID().toString())
                            .ratingId(ratingId)
                            .ratingBf(ratingBf)
                            .ratingAf(ratingAf)
                            .contentAf(contentAf)
                            .contentBf(contentBf)
                            .approveBy(approveBy)
                            .approveAt(approveAt)
                            .state(state)
                            .createAt(now)
                            .createBy(tuple.getT1().getUsername())
                            .isNew(true)
                            .build();

                    return ratingHistoryRepository.save(ratingHistory);
                });
    }
}
