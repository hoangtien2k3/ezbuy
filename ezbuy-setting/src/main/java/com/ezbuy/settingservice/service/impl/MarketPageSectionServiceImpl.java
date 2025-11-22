package com.ezbuy.settingservice.service.impl;

import static com.ezbuy.core.constants.ErrorCode.SUCCESS;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.request.SearchMarketPageSectionRequest;
import com.ezbuy.settingservice.model.entity.MarketPageSection;
import com.ezbuy.settingservice.model.dto.request.MarketPageSectionRequest;
import com.ezbuy.settingservice.model.dto.response.SearchMarketPageSectionResponse;
import com.ezbuy.settingservice.repository.MarketPageRepository;
import com.ezbuy.settingservice.repository.MarketPageSectionRepository;
import com.ezbuy.settingservice.repository.MarketSectionRepository;
import com.ezbuy.settingservice.repositoryTemplate.MarketPageSectionRepositoryTemplate;
import com.ezbuy.settingservice.service.MarketPageSectionService;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.TokenUser;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.Translator;

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
        return Mono.zip(SecurityUtils.getCurrentUser().switchIfEmpty(Mono.just(new TokenUser())),
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
            throw new BusinessException(ErrorCode.INVALID_PARAMS, "market.page.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.error(new BusinessException(ErrorCode.NOT_FOUND, "market.page.not.found"))),
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
                        ErrorCode.BAD_REQUEST, "market.page.error.pageId.not.found")))
                .flatMap(marketPages -> {
                    if (DataUtil.isNullOrEmpty(marketPages)) {
                        Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "market.page.error.service.not.found"));
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> validateExitsSectionId(String sectionId, Integer status) {
        return marketSectionRepository
                .findMarketSectionById(sectionId, status)
                .switchIfEmpty(Mono.error(new BusinessException(
                        ErrorCode.BAD_REQUEST, "market.page.error.sectionId.not.found")))
                .flatMap(marketPages -> {
                    if (DataUtil.isNullOrEmpty(marketPages)) {
                        Mono.error(new BusinessException(ErrorCode.BAD_REQUEST, "market.page.error.service.not.found"));
                    }
                    return Mono.just(true);
                });
    }
}
