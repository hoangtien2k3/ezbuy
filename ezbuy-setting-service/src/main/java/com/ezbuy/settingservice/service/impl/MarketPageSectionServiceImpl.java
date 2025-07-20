package com.ezbuy.settingservice.service.impl;

import static com.reactify.constants.CommonErrorCode.SUCCESS;

import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingmodel.model.MarketPageSection;
import com.ezbuy.settingmodel.request.MarketPageSectionRequest;
import com.ezbuy.settingmodel.response.SearchMarketPageSectionResponse;
import com.ezbuy.settingservice.repository.MarketPageRepository;
import com.ezbuy.settingservice.repository.MarketPageSectionRepository;
import com.ezbuy.settingservice.repository.MarketSectionRepository;
import com.ezbuy.settingservice.repositoryTemplate.MarketPageSectionRepositoryTemplate;
import com.ezbuy.settingservice.service.MarketPageSectionService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.TokenUser;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MarketPageSectionServiceImpl extends BaseServiceHandler implements MarketPageSectionService {
    private final MarketPageSectionRepository marketPageSectionRepository;
    private final MarketPageRepository marketPageRepository;
    private final MarketSectionRepository marketSectionRepository;
    private final MarketPageSectionRepositoryTemplate marketPageSectionRepositoryTemplate;

    @Override
    public Mono<SearchMarketPageSectionResponse> getMarketPageSection(SearchMarketPageSectionRequest request) {
        // validate request
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        Flux<MarketPageSection> lstMarketPageSection =
                marketPageSectionRepositoryTemplate.findMarketPageSection(request);
        Mono<Long> countMono = marketPageSectionRepositoryTemplate.countMarketPageSection(request);
        return Mono.zip(lstMarketPageSection.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            SearchMarketPageSectionResponse response = new SearchMarketPageSectionResponse();
            response.setLstMarketPageSection(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketPageSection>> createMarketPageSection(MarketPageSectionRequest request) {
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.just(new TokenUser())),
                        validateExitsPageId(request.getPageId(), 1),
                        validateExitsSectionId(request.getSectionId(), 1))
                .flatMap(zip -> {
                    LocalDateTime now = LocalDateTime.now();
                    TokenUser tokenUser = zip.getT1();
                    String marketPageSectionId = UUID.randomUUID().toString();
                    MarketPageSection marketPageSection = MarketPageSection.builder()
                            .id(marketPageSectionId)
                            .pageId(request.getPageId())
                            .sectionId(request.getSectionId())
                            .displayOrder(request.getDisplayOrder())
                            .status(request.getStatus())
                            .createBy(tokenUser.getUsername())
                            .createAt(now)
                            .updateBy(tokenUser.getUsername())
                            .updateAt(now)
                            .isNew(true)
                            .build();
                    return marketPageSectionRepository
                            .save(marketPageSection)
                            .onErrorReturn(new MarketPageSection())
                            .flatMap(result -> Mono.just(new DataResponse<>(SUCCESS, result)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketPageSection>> updateMarketPageSection(String id, MarketPageSectionRequest request) {
        if (DataUtil.isNullOrEmpty(id)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.page.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "market.page.not.found"))),
                        marketPageSectionRepository.getById(id))
                .flatMap(zip -> {
                    LocalDateTime now = LocalDateTime.now();
                    TokenUser tokenUser = zip.getT1();
                    MarketPageSection marketPageSection = zip.getT2();
                    marketPageSection.setSectionId(request.getSectionId());
                    marketPageSection.setDisplayOrder(request.getDisplayOrder());
                    marketPageSection.setStatus(request.getStatus());
                    marketPageSection.setUpdateAt(now);
                    marketPageSection.setUpdateBy(tokenUser.getUsername());
                    return marketPageSectionRepository
                            .save(marketPageSection)
                            .onErrorReturn(new MarketPageSection())
                            .flatMap(result -> Mono.just(new DataResponse<>(SUCCESS, result)));
                })
                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
    }

    private Mono<Boolean> validateExitsPageId(String pageId, Integer status) {
        return marketPageRepository
                .findMarketPageById(pageId, status)
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.BAD_REQUEST, Translator.toLocaleVi("market.page.error.pageId.not.found"))))
                .flatMap(marketPages -> {
                    if (DataUtil.isNullOrEmpty(marketPages)) {
                        Mono.error(new BusinessException(
                                CommonErrorCode.BAD_REQUEST,
                                Translator.toLocaleVi("market.page.error.service.not.found")));
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> validateExitsSectionId(String sectionId, Integer status) {
        return marketSectionRepository
                .findMarketSectionById(sectionId, status)
                .switchIfEmpty(Mono.error(new BusinessException(
                        CommonErrorCode.BAD_REQUEST, Translator.toLocaleVi("market.page.error.sectionId.not.found"))))
                .flatMap(marketPages -> {
                    if (DataUtil.isNullOrEmpty(marketPages)) {
                        Mono.error(new BusinessException(
                                CommonErrorCode.BAD_REQUEST,
                                Translator.toLocaleVi("market.page.error.service.not.found")));
                    }
                    return Mono.just(true);
                });
    }
}
