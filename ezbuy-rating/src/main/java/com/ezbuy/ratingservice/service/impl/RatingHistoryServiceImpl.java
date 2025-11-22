package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.ratingservice.model.entity.RatingHistory;
import com.ezbuy.ratingservice.repository.RatingHistoryRepository;
import com.ezbuy.ratingservice.service.RatingHistoryService;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.util.SecurityUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
            String state) {
        var getSysDate = ratingHistoryRepository.getSysDate();
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(
                    Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))), getSysDate)
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
