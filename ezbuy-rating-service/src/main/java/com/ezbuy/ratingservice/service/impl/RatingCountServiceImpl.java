package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.ratingmodel.dto.RatingDetailDTO;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingservice.repository.RatingCountRepository;
import com.ezbuy.ratingservice.service.RatingCountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.factory.ObjectMapperFactory;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.Translator;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingCountServiceImpl extends BaseServiceHandler implements RatingCountService {

    private final RatingCountRepository ratingCountRepository;
    private static final Long MAX_RATING = 5L;

    @Override
    public Mono<RatingCount> updateRatingCount(
            String ratingTypeCode, String targetId, Long newRatingPoint, Long oldRatingPoint) {
        Mono<RatingCount> ratingCount = ratingCountRepository.getRatingCountByTypeAndTargetId(ratingTypeCode, targetId);
        return ratingCount
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("find.rating.count.error"))))
                .flatMap(count -> {
                    String detail = count.getDetail();
                    try {
                        List<RatingDetailDTO> lstDetail =
                                ObjectMapperFactory.getInstance().readValue(detail, new TypeReference<>() {});
                        if (DataUtil.isNullOrEmpty(lstDetail)) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("detail.not.found")));
                        }
                        float totalRate = 0;
                        Long numberRate = 0L;
                        for (RatingDetailDTO detailDTO : lstDetail) {
                            Long numberRateDetail = detailDTO.getNumberRate();
                            // lay ra detail co so diem danh gia = gia tri danh gia truyen len
                            if (oldRatingPoint.equals(detailDTO.getRating())) {
                                numberRateDetail--;
                            } else if (newRatingPoint.equals(detailDTO.getRating())) {
                                numberRateDetail++;
                            }
                            detailDTO.setNumberRate(numberRateDetail);
                            numberRate += numberRateDetail;
                            totalRate += DataUtil.safeToFloat(detailDTO.getRating()) * numberRateDetail;
                        }
                        BigDecimal ratingAverage =
                                BigDecimal.valueOf(totalRate).divide(BigDecimal.valueOf(numberRate), RoundingMode.UP);
                        Float rating =
                                ratingAverage.setScale(1, RoundingMode.UP).floatValue();
                        count.setRating(rating);
                        String detailAfterCalculate = DataUtil.parseObjectToString(lstDetail);
                        count.setDetail(detailAfterCalculate);
                        return ratingCountRepository
                                .save(count)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("update.rating.count.error"))))
                                .flatMap(Mono::just);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("read.value.detail.error")));
                    }
                });
    }

    @Override
    public Mono<RatingCount> changeStatusRatingCount(
            String ratingTypeCode, String targetId, Long ratingPoint, Integer sumRateStatus) {
        Mono<RatingCount> ratingCount = ratingCountRepository.getRatingCountByTypeAndTargetId(ratingTypeCode, targetId);
        return ratingCount
                .flatMap(count -> {
                    String detail = count.getDetail();
                    try {
                        List<RatingDetailDTO> lstDetail =
                                ObjectMapperFactory.getInstance().readValue(detail, new TypeReference<>() {});
                        if (DataUtil.isNullOrEmpty(lstDetail)) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("detail.not.found")));
                        }
                        float totalRate = 0;
                        Long numberRate = 0L;
                        for (RatingDetailDTO detailDTO : lstDetail) {
                            Long numberRateDetail = detailDTO.getNumberRate();
                            // lay ra detail co so diem danh gia = gia tri danh gia truyen len
                            if (ratingPoint.equals(detailDTO.getRating())) {
                                if (Constants.Activation.ACTIVE.equals(sumRateStatus)) {
                                    numberRateDetail++;
                                } else {
                                    numberRateDetail--;
                                }
                                detailDTO.setNumberRate(numberRateDetail);
                            }
                            numberRate += numberRateDetail;
                            totalRate += DataUtil.safeToFloat(detailDTO.getRating()) * numberRateDetail;
                        }
                        BigDecimal ratingAverage =
                                BigDecimal.valueOf(totalRate).divide(BigDecimal.valueOf(numberRate), RoundingMode.UP);
                        Float rating =
                                ratingAverage.setScale(1, RoundingMode.UP).floatValue();
                        count.setRating(rating);
                        String detailAfterCalculate = DataUtil.parseObjectToString(lstDetail);
                        count.setDetail(detailAfterCalculate);
                        return ratingCountRepository.save(count);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, Translator.toLocaleVi("read.value.detail.error")));
                    }
                })
                .switchIfEmpty(createRatingCount(ratingPoint, ratingTypeCode, targetId));
    }

    private Mono<RatingCount> createRatingCount(Long rating, String ratingTypeCode, String targetId) {
        List<RatingDetailDTO> listRatingDetail = new ArrayList<>();
        for (int i = 1; i <= RatingCountServiceImpl.MAX_RATING; i++) {
            RatingDetailDTO ratingDetailDTO = new RatingDetailDTO();
            ratingDetailDTO.setRating((long) i);
            if (i == rating) {
                ratingDetailDTO.setNumberRate(1L);
            } else {
                ratingDetailDTO.setNumberRate(0L);
            }
            listRatingDetail.add(ratingDetailDTO);
        }
        String detail = DataUtil.parseObjectToString(listRatingDetail);
        RatingCount ratingCount = RatingCount.builder()
                .rating(DataUtil.safeToFloat(rating))
                .detail(detail)
                .maxRating(RatingCountServiceImpl.MAX_RATING)
                .ratingTypeCode(ratingTypeCode)
                .targetId(targetId)
                .numberRate(1L)
                .id(UUID.randomUUID().toString())
                .isNew(true)
                .build();
        return ratingCountRepository.save(ratingCount);
    }
}
