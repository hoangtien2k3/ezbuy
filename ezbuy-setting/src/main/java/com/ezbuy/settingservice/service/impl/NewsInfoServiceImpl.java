package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.NewsDetailDTO;
import com.ezbuy.settingservice.model.dto.NewsInfoDTO;
import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.RelateNewsDTO;
import com.ezbuy.settingservice.model.dto.request.SearchNewsInfoRequest;
import com.ezbuy.settingservice.model.entity.NewsInfo;
import com.ezbuy.settingservice.model.dto.request.CreateNewsInfoRequest;
import com.ezbuy.settingservice.model.dto.response.SearchNewsInfoResponse;
import com.ezbuy.settingservice.repository.NewsInfoRepository;
import com.ezbuy.settingservice.repositoryTemplate.NewsInfoRepositoryTemplate;
import com.ezbuy.settingservice.service.NewsInfoService;
import com.ezbuy.core.config.properties.MinioProperties;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.constants.Constants;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.MinioUtils;
import com.ezbuy.core.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsInfoServiceImpl extends BaseServiceHandler implements NewsInfoService {

    private final NewsInfoRepository newsInfoRepository;
    private final NewsInfoRepositoryTemplate newsInfoRepositoryTemplate;
    private final MinioUtils minioUtils;
    private final MinioProperties minioProperties;

    @Override
    @Transactional
    public Mono<DataResponse<NewsInfo>> createNewsInfo(CreateNewsInfoRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Integer displayOrder = request.getDisplayOrder();
        var getSysDate = newsInfoRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))),
                        validateExistNewsInfo(code, displayOrder, request.getGroupNewsId()),
                        getSysDate,
                        minioUtils.uploadMedia(request.getNavigatorUrl()))
                .flatMap(tuple -> {
                    String NewsInfoId = UUID.randomUUID().toString();
                    LocalDateTime now = tuple.getT3();
                    String title = DataUtil.safeTrim(request.getTitle());
                    String summary = DataUtil.safeTrim(request.getSummary());
                    String state = DataUtil.safeTrim(request.getState());
                    String userName = tuple.getT1().getUsername();
                    String url = tuple.getT4();
                    NewsInfo newsInfo = NewsInfo.builder()
                            .id(NewsInfoId)
                            .code(code)
                            .title(title)
                            .displayOrder(displayOrder)
                            .status(request.getStatus())
                            .summary(summary)
                            .state(state)
                            .groupNewsId(request.getGroupNewsId())
                            .createAt(now)
                            .createBy(userName)
                            .updateAt(now)
                            .updateBy(userName)
                            .navigatorUrl(url)
                            .build();
                    return newsInfoRepository
                            .save(newsInfo)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    ErrorCode.INTERNAL_SERVER_ERROR, "news.info.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", newsInfo)));
                });
    }

    public void validateInput(CreateNewsInfoRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.code.max.length");
        }
        String title = DataUtil.safeTrim(request.getTitle());
        if (DataUtil.isNullOrEmpty(title)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.name");
        }
        if (title.length() > 200) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.name.max.length");
        }
        Integer displayOrder = request.getDisplayOrder();
        if (displayOrder == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.order");
        }
        if (displayOrder < 1) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.news.info.order.min");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateExistNewsInfo(String code, Integer displayOrder, String groupNewsId) {
        return Mono.zip(newsInfoRepository.findByCode(code).defaultIfEmpty(new NewsInfo()),
                        newsInfoRepository.findByGroupOrder(displayOrder, groupNewsId).defaultIfEmpty(new NewsInfo()))
                .flatMap(tuple -> {
                    NewsInfo newsInfoByCode = tuple.getT1();
                    if (newsInfoByCode.getCode() != null) {
                        return Mono.error(
                                new BusinessException(ErrorCode.NOT_FOUND, "create.news.info.code.is.exists"));
                    }
                    NewsInfo newsInfoByDisplayOrder = tuple.getT2();
                    if (newsInfoByDisplayOrder.getDisplayOrder() != null) {
                        return Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "create.news.info.order.is.exits"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<NewsInfo>> editNewsInfo(String id, CreateNewsInfoRequest request) {
        validateInput(request);
        String newsInfoId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(newsInfoId)) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "news.info.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "user.null"))),
                        newsInfoRepository.getById(newsInfoId)
                                .switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "news.info.not.found"))))
                .flatMap(tuple -> {
                    NewsInfo newsInfo = tuple.getT2();
                    Mono<Boolean> checkExistGroupCode = Mono.just(true);
                    Mono<Boolean> checkExistGroupOrder = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    if (!DataUtil.safeEqual(code, newsInfo.getCode())) {
                        checkExistGroupCode = validateExistNewsInfo(code, null, null);
                    }
                    Integer displayOrder = request.getDisplayOrder();
                    if (!DataUtil.safeEqual(displayOrder, newsInfo.getDisplayOrder())) {
                        checkExistGroupOrder = validateExistNewsInfo(null, displayOrder, newsInfo.getGroupNewsId());
                    }
                    String navigatorUrl = DataUtil.safeTrim(request.getNavigatorUrl());
                    String urlFromDB = DataUtil.safeTrim(newsInfo.getNavigatorUrl());
                    Mono<String> url;
                    if (!DataUtil.safeEqual(navigatorUrl, urlFromDB)) {
                        url = minioUtils.uploadMedia(navigatorUrl);
                    } else {
                        url = Mono.just(navigatorUrl);
                    }
                    String title = DataUtil.safeTrim(request.getTitle());
                    String state = DataUtil.safeTrim(request.getState());
                    String summary = DataUtil.safeTrim(request.getSummary());
                    return Mono.zip(checkExistGroupCode, checkExistGroupOrder, url)
                            .flatMap(tuple2 -> {
                                String urlUpdate = urlFromDB;
                                if (!DataUtil.isNullOrEmpty(tuple2.getT3())) {
                                    urlUpdate = tuple2.getT3();
                                }
                                return newsInfoRepository.updateNewsInfo(
                                                newsInfoId,
                                                code,
                                                title,
                                                displayOrder,
                                                request.getStatus(),
                                                request.getGroupNewsId(),
                                                state,
                                                summary,
                                                tuple.getT1().getUsername(),
                                                urlUpdate)
                                        .defaultIfEmpty(new NewsInfo())
                                        .flatMap(response -> Mono.just(new DataResponse<>("success", null)));
                            });
                });
    }

    @Override
    @Transactional
    public Mono<SearchNewsInfoResponse> findNewsInfo(SearchNewsInfoRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        if (!Objects.isNull(request.getFromDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(ErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        Flux<NewsInfoDTO> NewsInfo = newsInfoRepositoryTemplate.findNewsInfo(request);
        Mono<Long> countMono = newsInfoRepositoryTemplate.countNewsInfo(request);
        return Mono.zip(NewsInfo.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchNewsInfoResponse response = new SearchNewsInfoResponse();
            response.setLstNewsInfoDTO(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<List<NewsInfo>> getAllNewsInfoActive() {
        return newsInfoRepository.getAllNewsInfoActive().collectList();
    }

    @Override
    public Mono<DataResponse<NewsDetailDTO>> getNewsDetailByNewsInfoId(String id) {
        return newsInfoRepositoryTemplate
                .getNewsDetailByNewsInfoId(id)
                .collectList()
                .map(newsDetailList -> {
                    NewsDetailDTO data = null;
                    if (!DataUtil.isNullOrEmpty(newsDetailList)) {
                        data = newsDetailList.getFirst();
                    }
                    return new DataResponse<>("success", data);
                });
    }

    @Override
    public Mono<DataResponse<List<RelateNewsDTO>>> getRelateNewsByGroupNewsId(String id) {
        return newsInfoRepositoryTemplate
                .getRelateNewsByGroupNewsId(id)
                .collectList()
                .map(relateNewsList -> new DataResponse<>("success", relateNewsList));
    }
}
