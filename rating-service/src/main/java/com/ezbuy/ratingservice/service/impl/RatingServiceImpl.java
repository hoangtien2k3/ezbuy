package com.ezbuy.ratingservice.service.impl;

import com.ezbuy.ratingmodel.dto.*;
import com.ezbuy.ratingmodel.model.Rating;
import com.ezbuy.ratingmodel.model.RatingCount;
import com.ezbuy.ratingmodel.model.RatingHistory;
import com.ezbuy.ratingmodel.request.FindRatingRequest;
import com.ezbuy.ratingmodel.request.RatingRequest;
import com.ezbuy.ratingmodel.response.SearchRatingResponse;
import com.ezbuy.ratingservice.repository.RatingCountRepository;
import com.ezbuy.ratingservice.repository.RatingRepository;
import com.ezbuy.ratingservice.repository.repoTemplate.RatingRepositoryTemplate;
import com.ezbuy.ratingservice.service.RatingCountService;
import com.ezbuy.ratingservice.service.RatingHistoryService;
import com.ezbuy.ratingservice.service.RatingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.constants.MessageConstant;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ModelMapperFactory;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl extends BaseServiceHandler implements RatingService {

    private final RatingCountRepository ratingCountRepository;
    private final RatingRepository ratingRepository;
    private final RatingRepositoryTemplate ratingRepositoryTemplate;
    private final RatingCountService ratingCountService;
    private final RatingHistoryService ratingHistoryService;

    @Override
    public Mono<DataResponse<RatingServiceResponse>> getRatingService(String serviceAlias) {
        return Mono.zip(
                        ratingRepository.getListRatingService(serviceAlias).collectList(),
                        ratingCountRepository
                                .getRatingCountService(Collections.singletonList(serviceAlias))
                                .collectList())
                .flatMap(listRating -> {
                    RatingServiceResponse ratingServiceResponse = new RatingServiceResponse();
                    List<Rating> lstRatingComment = listRating.getT1();
                    if (!DataUtil.isNullOrEmpty(lstRatingComment)) {
                        List<RatingCommentDTO> listRatingComment = new ArrayList<>();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        for (Rating ratingComment : lstRatingComment) {
                            RatingCommentDTO commentDTO = new RatingCommentDTO();
                            commentDTO.setRating(DataUtil.safeToFloat(ratingComment.getRating()));
                            commentDTO.setRatingDate(
                                    ratingComment.getRatingDate().format(formatter));
                            commentDTO.setCustName(ratingComment.getCustName());
                            commentDTO.setContent(ratingComment.getContent());
                            listRatingComment.add(commentDTO);
                        }
                        ratingServiceResponse.setListRatingComment(listRatingComment);
                    }

                    RatingCount ratingCount;
                    if (!DataUtil.isNullOrEmpty(listRating.getT2())) {
                        ratingCount = listRating.getT2().getFirst();
                        if (ratingCount != null) {
                            ratingServiceResponse.setRating(ratingCount.getRating());
                            ratingServiceResponse.setMaxRating(DataUtil.safeToFloat(ratingCount.getMaxRating()));
                            ratingServiceResponse.setNumberRate(ratingCount.getNumberRate());
                            ratingServiceResponse.setServiceAlias(serviceAlias);
                            try {
                                List<RatingDetailDTO> lstRatingDetail = ObjectMapperFactory.getInstance()
                                        .readValue(ratingCount.getDetail(), new TypeReference<>() {});
                                ratingServiceResponse.setListRatingDetail(lstRatingDetail);
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                            }
                        }
                    }
                    return Mono.just(
                            new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), ratingServiceResponse));
                });
    }

    @Override
    public Mono<List<RatingCount>> getAllRatingActive() {
        return ratingCountRepository
                .getAll()
                .collectList()
                .map(lstRating -> lstRating)
                .doOnError(e -> Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "telecom.not.found")));
    }

    @Override
    public Mono<DataResponse<Rating>> createRating(RatingRequest request) {
        // validate thong tin rating
        validateInput(request);
        var getSysDate = ratingRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT2();
                    String ratingId = UUID.randomUUID().toString();
                    Rating rating = ModelMapperFactory.getInstance().map(request, Rating.class);
                    rating.setId(ratingId);
                    rating.setCreateAt(now);
                    rating.setCreateBy(tuple.getT1().getUsername());
                    rating.setNew(true);
                    Mono<RatingCount> ratingCountMono = Mono.just(new RatingCount());
                    // cap nhat rating count neu sumRateStatus = 1
                    if (Constants.Activation.ACTIVE.equals(rating.getSumRateStatus())) {
                        ratingCountMono = ratingCountService.changeStatusRatingCount(
                                rating.getRatingTypeCode(),
                                rating.getTargetId(),
                                rating.getRating(),
                                rating.getSumRateStatus());
                    }
                    return Mono.zip(
                                    ratingCountMono,
                                    ratingRepository
                                            .save(rating)
                                            .switchIfEmpty(Mono.error(new BusinessException(
                                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                    "service.rating.insert.failed"))))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", x.getT2())));
                });
    }

    @Override
    public Mono<DataResponse<Rating>> editRating(String id, RatingRequest request) {
        validateInput(request);
        String finalId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(finalId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.id.not.empty");
        }
        var getSysDate = ratingRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        ratingRepository.getById(finalId)
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "service.rating.not.found"))), getSysDate)
                .flatMap(tuple -> {
                    String userUpdate = tuple.getT1().getUsername();
                    LocalDateTime now = tuple.getT3();
                    Rating rating = tuple.getT2();
                    Mono<RatingCount> ratingCountMono = Mono.just(new RatingCount());
                    if (!rating.getSumRateStatus().equals(request.getSumRateStatus())) {
                        if (Constants.Activation.ACTIVE.equals(request.getSumRateStatus())) {
                            ratingCountMono = ratingCountService.changeStatusRatingCount(
                                    request.getRatingTypeCode(),
                                    request.getTargetId(),
                                    request.getRating(),
                                    Constants.Activation.ACTIVE);
                        } else {
                            ratingCountMono = ratingCountService.changeStatusRatingCount(
                                    rating.getRatingTypeCode(),
                                    rating.getTargetId(),
                                    rating.getRating(),
                                    Constants.Activation.INACTIVE);
                        }
                    } else if (Constants.Activation.ACTIVE.equals(rating.getSumRateStatus())) {
                        if (!rating.getRating().equals(request.getRating())) {
                            ratingCountMono = ratingCountService.updateRatingCount(
                                    rating.getRatingTypeCode(),
                                    rating.getTargetId(),
                                    request.getRating(),
                                    rating.getRating());
                        }
                    }
                    Mono<RatingHistory> ratingHistoryMono = Mono.just(new RatingHistory());
                    if (!rating.getRating().equals(request.getRating())
                            || !rating.getContent().equals(request.getContent())
                            || !rating.getState().equals(request.getState())) {
                        ratingHistoryMono = ratingHistoryService.createRatingHistory(
                                rating.getId(),
                                rating.getRating(),
                                request.getRating(),
                                rating.getContent(),
                                request.getContent(),
                                request.getApproveBy(),
                                now,
                                request.getState());
                    }
                    ModelMapperFactory.getInstance().map(request, rating);
                    rating.setUpdateAt(now);
                    rating.setUpdateBy(userUpdate);
                    Mono<Rating> updateServiceRating = ratingRepository.save(rating);
                    return Mono.zip(ratingCountMono, ratingHistoryMono, updateServiceRating)
                            .flatMap(response -> Mono.just(new DataResponse<>("success", response.getT3())));
                });
    }

    @Override
    public Mono<SearchRatingResponse> findRating(FindRatingRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if (!Objects.isNull(request.getFromDate()) && !Objects.isNull(request.getToDate())) {
            if (request.getFromDate().compareTo(request.getToDate()) > 0) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        Flux<RatingDTO> serviceRatings = ratingRepositoryTemplate.findRating(request);
        Mono<Long> countMono = ratingRepositoryTemplate.countServiceRating(request);
        return Mono.zip(serviceRatings.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchRatingResponse response = new SearchRatingResponse();
            response.setLstRating(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    public void validateInput(RatingRequest request) {
        String username = DataUtil.safeTrim(request.getUsername());
        if (username.length() > 50) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.username.max.length");
        }
        String custName = DataUtil.safeTrim(request.getCustName());
        if (DataUtil.isNullOrEmpty(custName)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.custname.empty");
        }
        if (custName.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.custname.max.length");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "service.rating.status.error");
        }
    }

    @Override
    public Mono<List<RatingCount>> getRatingByAlias(List<String> alias) {
        return ratingCountRepository
                .getRatingCountService(alias)
                .collectList()
                .map(lstRating -> lstRating)
                .doOnError(e -> Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "telecom.not.found")));
    }

    @Override
    public Mono<DataResponse<RatingServiceResponse>> getRatingServicePaging(SearchRatingRequest request) {
        if (request == null) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.pageIndex.invalid"));
        }
        int pageIndex = DataUtil.validatePageIndex(request.getPageIndex(), request.getPageSize());
        request.setPageIndex(pageIndex);
        int pageSize = DataUtil.validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if (DataUtil.isNullOrEmpty(request.getRatingFind())) {
            request.setRatingFind(0F);
        }
        return Mono.zip(ratingRepository.getListRatingServicePage(
                                        request.getServiceAlias(),
                                        request.getPageSize(),
                                        request.getPageIndex(),
                                        request.getRatingFind()).collectList(),
                        ratingCountRepository.getRatingCountService(Collections.singletonList(request.getServiceAlias())).collectList(),
                        ratingRepository.getCountRatingService(request.getServiceAlias(), request.getRatingFind()))
                .flatMap(listRating -> {
                    RatingServiceResponse ratingServiceResponse = new RatingServiceResponse();
                    List<Rating> lstRatingComment = listRating.getT1();
                    if (!DataUtil.isNullOrEmpty(lstRatingComment)) {
                        List<RatingCommentDTO> listRatingComment = new ArrayList<>();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        for (Rating ratingComment : lstRatingComment) {
                            RatingCommentDTO commentDTO = new RatingCommentDTO();
                            commentDTO.setRating(DataUtil.safeToFloat(ratingComment.getRating()));
                            commentDTO.setRatingDate(
                                    ratingComment.getRatingDate().format(formatter));
                            commentDTO.setCustName(ratingComment.getCustName());
                            commentDTO.setContent(ratingComment.getContent());
                            listRatingComment.add(commentDTO);
                        }
                        ratingServiceResponse.setListRatingComment(listRatingComment);
                    }

                    RatingCount ratingCount;
                    if (!DataUtil.isNullOrEmpty(listRating.getT2())) {
                        ratingCount = listRating.getT2().getFirst();
                        if (ratingCount != null) {
                            ratingServiceResponse.setRating(ratingCount.getRating());
                            ratingServiceResponse.setMaxRating(DataUtil.safeToFloat(ratingCount.getMaxRating()));
                            ratingServiceResponse.setNumberRate(ratingCount.getNumberRate());
                            ratingServiceResponse.setServiceAlias(request.getServiceAlias());
                            try {
                                List<RatingDetailDTO> lstRatingDetail = ObjectMapperFactory.getInstance()
                                        .readValue(ratingCount.getDetail(), new TypeReference<>() {});
                                ratingServiceResponse.setListRatingDetail(lstRatingDetail);
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                            }
                        }
                    }
                    PaginationDTO pagination = new PaginationDTO();
                    pagination.setPageIndex(request.getPageIndex());
                    pagination.setPageSize(request.getPageSize());
                    pagination.setTotalRecords(listRating.getT3());
                    ratingServiceResponse.setPagination(pagination);
                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), ratingServiceResponse));
                });
    }
}
