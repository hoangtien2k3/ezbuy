package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.*;
import com.ezbuy.settingmodel.model.MarketSection;
import com.ezbuy.settingmodel.model.ServiceMedia;
import com.ezbuy.settingmodel.request.CreateMarketSectionRequest;
import com.ezbuy.settingmodel.request.MarketSectionSearchRequest;
import com.ezbuy.settingmodel.response.MarketSectionSearchResponse;
import com.ezbuy.settingservice.constant.SettingConstant;
import com.ezbuy.settingservice.repository.MarketSectionRepository;
import com.ezbuy.settingservice.repository.ServiceMediaRepository;
import com.ezbuy.settingservice.repositoryTemplate.MarketSectionRepositoryTemplate;
import com.ezbuy.settingservice.service.MarketSectionService;
import com.reactify.config.MinioProperties;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.model.TokenUser;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.MinioUtils;
import com.reactify.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MarketSectionServiceImpl extends BaseServiceHandler implements MarketSectionService {

    private final MarketSectionRepository marketSectionRepository;
    private final MarketSectionRepositoryTemplate marketSectionRepositoryTemplate;
    private final MinioUtils minioUtils;
    private final MinioProperties minioProperties;
    private final ServiceMediaRepository serviceMediaRepository;

    @Override
    public Flux<MarketSection> getMarketSection(String pageCode, String serviceId) {
        if (DataUtil.isNullOrEmpty(pageCode) || DataUtil.isNullOrEmpty(serviceId)) {
            return Flux.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }
        return this.marketSectionRepository.getMarketSection(pageCode, serviceId);
    }

    // lay danh sach cau hinh thanh phan trang theo pageCode va serviceAlias
    @Override
    public Flux<MarketSection> getMarketSectionV2(String pageCode, String serviceAlias) {
        if (DataUtil.isNullOrEmpty(pageCode) || DataUtil.isNullOrEmpty(serviceAlias)) {
            return Flux.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }
        return this.marketSectionRepository.getMarketSectionV2(pageCode, serviceAlias);
    }

    @Override
    public Mono<MarketSection> findById(String sectionId) {
        return this.marketSectionRepository.findById(sectionId).log();
    }

    @Override
    public Mono<MarketSectionSearchResponse> searchMarketSection(MarketSectionSearchRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.date.request.invalid");
        }
        if (!Objects.isNull(request.getFromDate()) && request.getFromDate().isAfter(request.getToDate())) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "params.from-date.larger.to-date");
        }

        Flux<MarketSection> pages = marketSectionRepositoryTemplate.queryMarketSections(request);
        Mono<Long> countMono = marketSectionRepositoryTemplate.countMarketSections(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            MarketSectionSearchResponse response = new MarketSectionSearchResponse();
            response.setMarketSection(zip.getT1());
            response.setPagination(pagination);

            return response;
        });
    }

    @Override
    @Transactional
    public Mono<MarketSectionDTO> findMarketSectionById(String id) {
        return marketSectionRepository
                .findMarketSectionById(id, null)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "data.not.found")))
                .map(marketSection -> {
                    MarketSectionDTO dto = new MarketSectionDTO();
                    BeanUtils.copyProperties(marketSection, dto);
                    return dto;
                });
    }

    private void validateInput(CreateMarketSectionRequest request) {
        String type = DataUtil.safeTrim(request.getType());
        if (DataUtil.isNullOrEmpty(type)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.type.null");
        }
        if (!SettingConstant.TEAMPLATE_TYPE.contains(type)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.error.type.invalid");
        }
        if (type.length() > 100) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.type.max.length");
        }
        String code = DataUtil.safeTrim(request.getCode());
        if (DataUtil.isNullOrEmpty(code)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.code.null");
        }
        if (code.length() > 100) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.code.max.length");
        }
        Long displayOrder = request.getDisplayOrder();
        if (displayOrder == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.display.null");
        }
        Integer status = request.getStatus();
        if (status == null) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.status.null");
        }
        if (!Constants.Activation.ACTIVE.equals(status) && !Constants.Activation.INACTIVE.equals(status)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.status.error");
        }
        String data = request.getData();
        if (!DataUtil.isNullOrEmpty(data) && !DataUtil.isValidFormatJson(data)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.data.error.format");
        }
    }

    public Mono<Boolean> validateExistCode(String code, Boolean isInsert, String id) {
        return marketSectionRepository
                .getByCode(code)
                .defaultIfEmpty(new MarketSection())
                .flatMap(marketSectionByCode -> {
                    if ((isInsert && marketSectionByCode.getCode() != null)
                            || (!isInsert
                                    && marketSectionByCode.getCode() != null
                                    && !DataUtil.safeEqual(marketSectionByCode.getId(), id))) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "market.section.validate.code.is.exist"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketSection>> createMarketSection(CreateMarketSectionRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Long displayOrder = request.getDisplayOrder();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                .switchIfEmpty(
                                        Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                        validateExistCode(code, true, null))
                .flatMap(
                        userValidate -> { // validate trung thong tin code hoac
                            // display order
                            String type = request.getType();
                            String data = request.getData();

                            return doUploadMedia(type, data, userValidate.getT1());
                        })
                .flatMap(data -> {
                    String marketSectionId = UUID.randomUUID().toString();
                    LocalDateTime now = LocalDateTime.now();
                    MarketSection marketSection = MarketSection.builder()
                            .id(marketSectionId)
                            .code(code)
                            .name(DataUtil.safeTrim(request.getName()))
                            .type(DataUtil.safeTrim(request.getType()))
                            .description(DataUtil.safeTrim(request.getDescription()))
                            .displayOrder(displayOrder)
                            .data(data.getT2())
                            .status(request.getStatus())
                            .createAt(now)
                            .createBy(data.getT1().getUsername())
                            .updateAt(now)
                            .updateBy(data.getT1().getUsername())
                            .isNew(true)
                            .build();
                    return marketSectionRepository
                            .save(marketSection)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "market.section.insert.failed")))
                            .flatMap(x -> Mono.just(new DataResponse<>("success", null)));
                });
    }

    private Mono<Tuple2<TokenUser, String>> doUploadMedia(String type, String data, TokenUser user) {
        if (DataUtil.safeEqual(type, "HEADER_INFO")) {
            HeaderInfoDTO headerInfo = DataUtil.parseStringToObject(data, HeaderInfoDTO.class);
            if (headerInfo == null || DataUtil.isNullOrEmpty(headerInfo.getIconUrl())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "market.section.error.header.info.icon.url.empty"));
            }
            return minioUtils.uploadMedia(headerInfo.getIconUrl(), minioProperties.getBucket())
                    .switchIfEmpty(Mono.error(new BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR, "market.section.error.insert.market.section.fail")))
                    .flatMap(url -> {
                        headerInfo.setIconUrl(url);
                        // insert service media
                        LocalDateTime now = LocalDateTime.now();
                        ServiceMedia serviceMedia = new ServiceMedia();
                        serviceMedia.setId(UUID.randomUUID().toString());
                        serviceMedia.setServiceId(headerInfo.getServiceId());
                        serviceMedia.setType("image");
                        serviceMedia.setUrl(url);
                        serviceMedia.setContentType(url);
                        serviceMedia.setStatus(1);
                        serviceMedia.setCreateAt(now);
                        serviceMedia.setCreateBy(user.getUsername());
                        serviceMedia.setCreateAt(now);
                        serviceMedia.setCreateBy(user.getUsername());
                        return Mono.zip(
                                Mono.just(user),
                                Mono.just(DataUtil.parseObjectToString(headerInfo)),
                                serviceMediaRepository.save(serviceMedia));
                    });
        }
        if (DataUtil.safeEqual(type, "SLIDE")) {
            SlideDTO slideDTO = DataUtil.parseStringToObject(data, SlideDTO.class);
            if (slideDTO == null || DataUtil.isNullOrEmpty(slideDTO.getMedias())) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "market.section.error.slide.media.empty"));
            }
            // create param list for change promotion
            List<Mono<MediaDTO>> mediaList = slideDTO.getMedias().stream()
                    .map(slide -> uploadMediaForSlide(slide, user.getUsername(), user.getUsername()))
                    .collect(Collectors.toList());
            return Mono.zip(mediaList, listResp -> {
                List<MediaDTO> resultList = new ArrayList<>();
                Arrays.stream(listResp).forEach(resp -> resultList.add((MediaDTO) resp));
                slideDTO.setMedias(resultList);
                return Tuples.of(user, DataUtil.parseObjectToString(slideDTO));
            });
        }
        return Mono.zip(Mono.just(user), Mono.just(data));
    }

    private Mono<MediaDTO> uploadMediaForSlide(MediaDTO mediaDTO, String createUser, String updateUser) {
        if (DataUtil.isNullOrEmpty(mediaDTO.getUrl())) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.error.slide.media.empty");
        }
        return minioUtils.uploadMedia(mediaDTO.getUrl(), minioProperties.getBucket())
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR, "market.section.error.insert.market.section.fail")))
                .flatMap(url -> {
                    // insert service media
                    LocalDateTime now = LocalDateTime.now();
                    ServiceMedia serviceMedia = new ServiceMedia();
                    serviceMedia.setId(UUID.randomUUID().toString());
                    serviceMedia.setType(mediaDTO.getType());
                    serviceMedia.setUrl(url);
                    serviceMedia.setContentType(url);
                    serviceMedia.setStatus(1);
                    serviceMedia.setCreateAt(now);
                    serviceMedia.setCreateBy(createUser);
                    serviceMedia.setCreateAt(now);
                    serviceMedia.setCreateBy(updateUser);
                    mediaDTO.setUrl(url);
                    return serviceMediaRepository.save(serviceMedia).map(insert -> {
                        mediaDTO.setMediaId(insert.getId());
                        return mediaDTO;
                    });
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketSection>> editMarketSection(String id, CreateMarketSectionRequest request) {
        // validate input
        validateInput(request);
        String code = DataUtil.safeTrim(request.getCode());
        Long displayOrder = request.getDisplayOrder();
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // get info user
                                // .switchIfEmpty(Mono.error(new
                                // BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                                .switchIfEmpty(Mono.just(new TokenUser())),
                        validateExistCode(code, false, id))
                .flatMap(
                        userValidate -> { // validate trung thong tin code hoac display order
                            String type = request.getType();
                            String data = request.getData();
                            return doUploadMedia(type, data, userValidate.getT1());
                        })
                .flatMap(data -> marketSectionRepository
                        .editMarketSection(
                                id,
                                DataUtil.safeTrim(request.getType()),
                                code,
                                DataUtil.safeTrim(request.getName()),
                                DataUtil.safeTrim(request.getDescription()),
                                displayOrder,
                                data.getT2(),
                                request.getStatus(),
                                data.getT1().getUsername())
                        .defaultIfEmpty(new MarketSection())
                        .switchIfEmpty(Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, "market.section.update.failed")))
                        .flatMap(x -> Mono.just(new DataResponse<>("success", null))));
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketSection>> deleteMarketSection(String id) {
        String marketSectionId = DataUtil.safeTrim(id);
        if (DataUtil.isNullOrEmpty(marketSectionId)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.section.validate.id.null");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser() // lay thong tin user
                                // .switchIfEmpty(Mono.error(new
                                // BusinessException(CommonErrorCode.NOT_FOUND, "user.null"))),
                                .switchIfEmpty(Mono.just(new TokenUser())),
                        marketSectionRepository
                                .findMarketSectionById(marketSectionId, Constants.Activation.ACTIVE) // lay
                                // thong
                                // tin
                                // marketSection
                                // theo
                                // id
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND, "market.section.validate.find.by.id.null"))))
                .flatMap(tuple -> marketSectionRepository
                        .updateStatus(
                                marketSectionId,
                                Constants.Activation.INACTIVE,
                                tuple.getT1().getUsername())
                        .defaultIfEmpty(new MarketSection())
                        .flatMap(response -> Mono.just(new DataResponse<>("success", null))));
    }

    @Override
    public Mono<List<MarketSection>> getAllActiveMarketSections() {
        Flux<MarketSection> marketSections = marketSectionRepository.getAllActiveMarketSections();
        return marketSections.collectList().map(result -> result);
    }

    @Override
    public Mono<List<MarketSection>> getAllActiveMarketSectionsRT() {
        Flux<MarketSection> marketSections = marketSectionRepository.getAllActiveMarketSectionsByTypeRichText();
        return marketSections.collectList().map(result -> result);
    }

    @Override
    public Mono<List<MarketSection>> getAllMarketSections() {
        Flux<MarketSection> marketSections = marketSectionRepository.getAllMarketSections();
        return marketSections.collectList().map(result -> result);
    }

    @Override
    public Mono<MarketSection> findByContentSectionId(String id) {
        return marketSectionRepository
                .findMarketSectionByContentSectionId(id)
                .switchIfEmpty(Mono.just(new MarketSection()))
                .map(marketSection -> marketSection);
    }
}
