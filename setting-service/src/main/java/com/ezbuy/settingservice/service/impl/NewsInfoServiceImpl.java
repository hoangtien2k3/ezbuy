package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.NewsDetailDTO;
import com.ezbuy.settingmodel.dto.NewsInfoDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.RelateNewsDTO;
import com.ezbuy.settingmodel.dto.request.SearchNewsInfoRequest;
import com.ezbuy.settingmodel.model.NewsInfo;
import com.ezbuy.settingmodel.request.CreateNewsInfoRequest;
import com.ezbuy.settingmodel.response.SearchNewsInfoResponse;
import com.ezbuy.settingservice.repository.NewsInfoRepository;
import com.ezbuy.settingservice.repositoryTemplate.NewsInfoRepositoryTemplate;
import com.ezbuy.settingservice.service.NewsInfoService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.MinioUtils;
import com.reactify.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsInfoServiceImpl extends BaseServiceHandler implements NewsInfoService {
    private final NewsInfoRepository newsInfoRepository;
    private final NewsInfoRepositoryTemplate newsInfoRepositoryTemplate;
    private final MinioUtils minioUtils;

    @Override
    @Transactional
    public Mono<DataResponse<NewsInfo>> createNewsInfo(CreateNewsInfoRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Integer displayOrder = request.getDisplayOrder();
        var getSysDate = newsInfoRepository.getSysDate();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistNewsInfo(code, displayOrder, request.getGroupNewsId()),
                        getSysDate,
                        uploadImage(request.getNavigatorUrl()))
                .flatMap(
                        tuple -> { // validate ton tai thong tin code hoac
                            // displayOrder
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
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, "news.info.insert.failed")))
                                    .flatMap(x -> Mono.just(new DataResponse<>("success", newsInfo)));
                        });
    }

    public void validateInput(CreateNewsInfoRequest request) {
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.code.empty");
        }
        if (code.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.code.max.length");
        }
        String title = DataUtil.safeTrim(request.getTitle());
        if (DataUtil.isNullOrEmpty(title)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.name");
        }
        if (title.length() > 200) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.name.max.length");
        }
        Integer displayOrder = request.getDisplayOrder();
        if (displayOrder == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.order");
        }
        if (displayOrder < 1) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.news.info.order.min");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "create.service.status.error");
        }
    }

    public Mono<Boolean> validateExistNewsInfo(String code, Integer displayOrder, String groupNewsId) {
        return Mono.zip(
                        newsInfoRepository.findByCode(code).defaultIfEmpty(new NewsInfo()),
                        newsInfoRepository
                                .findByGroupOrder(displayOrder, groupNewsId)
                                .defaultIfEmpty(new NewsInfo()))
                .flatMap(tuple -> {
                    NewsInfo newsInfoByCode = tuple.getT1();
                    if (newsInfoByCode.getCode() != null) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.news.info.code.is.exists"));
                    }
                    NewsInfo newsInfoByDisplayOrder = tuple.getT2();
                    if (newsInfoByDisplayOrder.getDisplayOrder() != null) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.NOT_FOUND, "create.news.info.order.is.exits"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<NewsInfo>> editNewsInfo(String id, CreateNewsInfoRequest request) {
        // validate input
        validateInput(request);
        String newsInfoId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(newsInfoId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "news.info.id.not.empty");
        }

        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        newsInfoRepository
                                .getById(newsInfoId) // lay thong tin NewsInfo theo id
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "news.info.not.found"))))
                .flatMap(tuple -> {
                    NewsInfo newsInfo = tuple.getT2();
                    Mono<Boolean> checkExistGroupCode = Mono.just(true);
                    Mono<Boolean> checkExistGroupOrder = Mono.just(true);
                    String code = DataUtil.safeTrim(request.getCode());
                    // check trung code
                    if (!DataUtil.safeEqual(code, newsInfo.getCode())) {
                        checkExistGroupCode = validateExistNewsInfo(code, null, null);
                    }
                    // check trung displayOrder
                    Integer displayOrder = request.getDisplayOrder();
                    if (!DataUtil.safeEqual(displayOrder, newsInfo.getDisplayOrder())) {
                        checkExistGroupOrder = validateExistNewsInfo(null, displayOrder, newsInfo.getGroupNewsId());
                    }
                    String navigatorUrl = DataUtil.safeTrim(request.getNavigatorUrl());
                    String urlFromDB = DataUtil.safeTrim(newsInfo.getNavigatorUrl());
                    Mono<String> url;
                    if (!DataUtil.safeEqual(navigatorUrl, urlFromDB)) {
                        url = uploadImage(navigatorUrl);
                    } else {
                        url = Mono.just(navigatorUrl);
                    }
                    // thuc hien cap nhat
                    String title = DataUtil.safeTrim(request.getTitle());
                    String state = DataUtil.safeTrim(request.getState());
                    String summary = DataUtil.safeTrim(request.getSummary());
                    return Mono.zip(checkExistGroupCode, checkExistGroupOrder, url)
                            .flatMap(tuple2 -> {
                                String urlUpdate = urlFromDB;
                                if (!DataUtil.isNullOrEmpty(tuple2.getT3())) {
                                    urlUpdate = tuple2.getT3();
                                }
                                return newsInfoRepository
                                        .updateNewsInfo(
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
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        // validate bat buoc nhap tu ngay den ngay
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        // validate tu ngay khong duoc lon hon den ngay
        if (!Objects.isNull(request.getFromDate())) {
            if (request.getFromDate().isAfter(request.getToDate())) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
            }
        }
        // tim kiem thong tin theo input
        Flux<NewsInfoDTO> NewsInfo = newsInfoRepositoryTemplate.findNewsInfo(request);
        // lay tong so luong ban ghi
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

    /**
     * save news content image
     *
     * @param dataImage
     *            image dataImage base64
     * @return image dataImage
     */
    private Mono<String> uploadImage(String dataImage) {
        if (!dataImage.startsWith("data:")) {
            return Mono.just(dataImage);
        }
        String base64Data = dataImage.split(",")[1];
        String base64Head = dataImage.split(",")[0];

        String extend = base64Head.split("/")[1].split(";")[0];
        String path = UUID.randomUUID() + "_." + extend;
        // byte[] bytes = Base64.getDecoder().decode(base64Data);
        byte[] bytes = Base64.decodeBase64(base64Data);

        String returnUrl = minioUtils.getMinioProperties().getPublicUrl() + "/"
                + Constants.MINIO_BUCKET_MARKET_INFO.URL_IMAGE + "/" + path;
        return minioUtils
                .uploadFile(bytes, Constants.MINIO_BUCKET_MARKET_INFO.URL_IMAGE, path)
                .thenReturn(returnUrl);
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
