package com.ezbuy.settingservice.service.impl;

import static io.hoangtien2k3.reactify.constants.CommonErrorCode.SUCCESS;

import com.ezbuy.settingmodel.dto.MarketPageDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketPage;
import com.ezbuy.settingmodel.request.MarketPageRequest;
import com.ezbuy.settingmodel.request.SearchMarketPageRequest;
import com.ezbuy.settingmodel.response.SearchMarketPageResponse;
import com.ezbuy.settingservice.repository.MarketPageRepository;
import com.ezbuy.settingservice.repositoryTemplate.MarketPageRepositoryTemplate;
import com.ezbuy.settingservice.service.MarketPageService;
import com.ezbuy.settingservice.service.TelecomService;
import io.hoangtien2k3.reactify.DataUtil;
import io.hoangtien2k3.reactify.SecurityUtils;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.constants.CommonErrorCode;
import io.hoangtien2k3.reactify.exception.BusinessException;
import io.hoangtien2k3.reactify.model.TokenUser;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.time.LocalDateTime;
import java.util.List;
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
@Transactional
public class MarketPageServiceImpl extends BaseServiceHandler implements MarketPageService {
    private final MarketPageRepository marketPageRepository;
    private final MarketPageRepositoryTemplate marketPageRepositoryTemplate;
    private final TelecomService telecomService;

    private Mono<Boolean> validateDuplicateServiceId(String telecomServiceId, String serviceAlias) {
        return marketPageRepository
                .findByServiceId(telecomServiceId)
                .collectList()
                .flatMap(marketPages -> {
                    if (!marketPages.isEmpty()) {
                        return telecomService
                                .getByOriginId(telecomServiceId, serviceAlias)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi("market.page.error.service.not.found"))))
                                .map(listDataResponse -> true);
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> validateDuplicateServiceAlias(String serviceAlias) {
        return marketPageRepository
                .findByServiceAlias(serviceAlias)
                .collectList()
                .flatMap(marketPages -> {
                    if (!marketPages.isEmpty()) {
                        return telecomService
                                .getByServiceAlias(serviceAlias)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi("market.page.error.service.not.found"))))
                                .map(listDataResponse -> true);
                    }
                    return Mono.just(true);
                });
    }

    @Override
    @Transactional
    public Mono<SearchMarketPageResponse> searchMarketPage(SearchMarketPageRequest request) {
        int pageIndex = DataUtil.safeToInt(request.getPageIndex(), 1);
        int pageSize = DataUtil.safeToInt(request.getPageSize(), 10);
        if (pageIndex < 1) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "pageIndex.invalid"));
        }
        if (pageSize > 100) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "pageSize.invalid"));
        }
        request.setPageIndex(pageIndex);
        request.setPageSize(pageSize);
        Flux<MarketPageDTO> pages = marketPageRepositoryTemplate.queryList(request);
        Mono<Long> countMono = marketPageRepositoryTemplate.count(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex((request.getPageIndex()));
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());
            SearchMarketPageResponse response = new SearchMarketPageResponse();
            response.setMarketPage(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<List<MarketPage>> getAllMarketPage() {
        return marketPageRepository.findAllMarketPage().collectList();
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketPage>> getMarketPage(String id) {
        return marketPageRepository
                .getById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.page.not.found")))
                .map(marketPage -> new DataResponse<>(Translator.toLocale("Success"), marketPage));
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketPage>> createMarketPage(MarketPageRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String code = DataUtil.safeTrim(request.getCode());
        // bo sung validate cho serviceAlias, khong bi anh huong neu serviceAlias null
        var validateDuplicateService = DataUtil.isNullOrEmpty(request.getServiceAlias())
                ? validateDuplicateServiceId(request.getServiceId(), request.getServiceAlias())
                : validateDuplicateServiceAlias(request.getServiceAlias());
        return Mono.zip(
                        SecurityUtils.getCurrentUser().switchIfEmpty(Mono.just(new TokenUser())),
                        validateDuplicateService)
                .flatMap(zip -> {
                    TokenUser tokenUser = zip.getT1();
                    String marketPageId = UUID.randomUUID().toString();
                    MarketPage marketPage = MarketPage.builder()
                            .id(marketPageId)
                            .serviceId(request.getServiceId())
                            .code(code)
                            .name(DataUtil.safeTrim(request.getName()))
                            .description(DataUtil.safeTrim(request.getDescription()))
                            .status(request.getStatus())
                            .createBy(tokenUser.getUsername())
                            .createAt(now)
                            .updateBy(tokenUser.getUsername())
                            .updateAt(now)
                            .serviceAlias(request.getServiceAlias()) // serviceAlias cua dich vu
                            // PYCXXX/LuongToanTrinhScontract
                            .build();
                    return marketPageRepository
                            .save(marketPage)
                            .onErrorReturn(new MarketPage())
                            .flatMap(result -> Mono.just(new DataResponse<>("success", result)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketPage>> updateMarketPage(MarketPageRequest request) {
        String id = request.getId();
        if (DataUtil.isNullOrEmpty(id)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.page.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "market.page.not.found"))),
                        marketPageRepository.getById(id))
                .flatMap(zip -> {
                    TokenUser tokenUser = zip.getT1();
                    MarketPage marketPage = zip.getT2();
                    Mono<Boolean> checkExistServiceId = Mono.just(true);

                    // bo sung them serviceAlias
                    // neu serviceAlias null thi se khong co dieu kien serviceAlias trong cau sql
                    if (!DataUtil.safeEqual(request.getServiceId(), marketPage.getServiceId())) {
                        checkExistServiceId =
                                validateDuplicateServiceId(request.getServiceId(), request.getServiceAlias());
                    }

                    // neu serviceAlias null thi khong cap nhat
                    if (DataUtil.isNullOrEmpty(request.getServiceAlias())) {
                        request.setServiceAlias(marketPage.getServiceAlias());
                    }

                    Mono<MarketPage> updateMarketPageMono = marketPageRepository
                            .updateMarketPage(
                                    request.getServiceId(),
                                    request.getCode(),
                                    request.getName(),
                                    request.getDescription(),
                                    request.getStatus(),
                                    tokenUser.getUsername(),
                                    id,
                                    request.getServiceAlias())
                            .defaultIfEmpty(new MarketPage()); // bo sung cap nhat serviceAlias
                    return Mono.zip(checkExistServiceId, updateMarketPageMono)
                            .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null)));
                })
                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
    }

    @Override
    public Mono<List<MarketPage>> getAllActiveMarketPage() {
        return marketPageRepository.getAllActiveMarketPage().collectList().map(list -> list);
    }

    public Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceId(List<String> lstServiceId) {
        return marketPageRepository
                .getByServiceId(lstServiceId)
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.page.not.found")))
                .map(marketPage -> new DataResponse<>(Translator.toLocale("Success"), marketPage));
    }

    // lay danh sach cau hinh trang theo danh sach alias
    @Override
    public Mono<DataResponse<List<MarketPage>>> getMarketPageByServiceIdV2(List<String> lstAlias) {
        return marketPageRepository
                .getByServiceAlias(lstAlias)
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.page.not.found")))
                .map(marketPage -> new DataResponse<>(Translator.toLocale("Success"), marketPage));
    }
}
