package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingservice.model.dto.PaginationDTO;
import com.ezbuy.settingservice.model.dto.TelecomDTO;
import com.ezbuy.settingservice.model.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingservice.model.dto.request.GetServiceConfigRequest;
import com.ezbuy.settingservice.model.dto.response.ClientTelecom;
import com.ezbuy.settingservice.model.dto.response.PageResponse;
import com.ezbuy.settingservice.model.dto.response.TelecomClient;
import com.ezbuy.settingservice.model.dto.response.TelecomPagingResponse;
import com.ezbuy.settingservice.model.dto.response.TelecomResponse;
import com.ezbuy.settingservice.model.entity.MarketInfo;
import com.ezbuy.settingservice.model.entity.Telecom;
import com.ezbuy.settingservice.model.dto.request.PageTelecomRequest;
import com.ezbuy.settingservice.model.dto.request.StatusLockingRequest;
import com.ezbuy.settingservice.model.dto.request.TelecomSearchingRequest;
import com.ezbuy.settingservice.repository.MarketInfoRepository;
import com.ezbuy.settingservice.repository.TelecomRepository;
import com.ezbuy.settingservice.repository.TelecomServiceConfigRep;
import com.ezbuy.settingservice.repositoryTemplate.TelecomRepositoryTemplate;
import com.ezbuy.settingservice.service.TelecomService;
import com.ezbuy.core.cache.LocalCache;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.Translator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelecomServiceImpl extends BaseServiceHandler implements TelecomService {

    private final TelecomRepositoryTemplate telecomRepositoryTemplate;
    private final TelecomRepository telecomRepository;

    @Lazy
    private final MarketInfoRepository marketInfoRepository;

    private final TelecomServiceConfigRep telecomServiceConfigRep;

    @Override
    @LocalCache(autoCache = true, maxRecord = 10000)
    public Mono<DataResponse<List<TelecomDTO>>> getTelecomService(
            List<String> ids, List<String> aliases, List<String> origins) {
        return this.telecomRepositoryTemplate
                .getAll(ids, aliases, origins)
                .collectList()
                .flatMap(telecoms -> Mono.just(new DataResponse<>("success", telecoms)));
    }

    @Override
    public Mono<TelecomPagingResponse> searchTelecomService(TelecomSearchingRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);

        Flux<Telecom> telecomFlux = telecomRepositoryTemplate.queryTelecomServices(request);
        Mono<Long> countMono = telecomRepositoryTemplate.countTelecomServices(request);
        return Mono.zip(telecomFlux.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());

            TelecomPagingResponse response = new TelecomPagingResponse();
            response.setTelecoms(zip.getT1());
            response.setPagination(pagination);

            return response;
        });
    }

    @Override
    public Mono<List<Telecom>> getNonFilterTelecom() {
        return telecomRepository.getNonFilterTelecom().collectList();
    }

    @Override
    public Mono<DataResponse<Telecom>> updateStatus(StatusLockingRequest params) {
        return SecurityUtils.getCurrentUser()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "user.null")))
                .flatMap(user -> telecomRepository
                        .getById(params.getId())
                        .switchIfEmpty(
                                Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "telecom.not.found")))
                        .flatMap(p -> {
                            telecomRepository.updateStatus(params.getId(), params.getStatus(), user.getId()).subscribe();
                            return Mono.just(new DataResponse<>(null, "success", null));
                        }));
    }

    @Override
    public Mono<DataResponse> initFilter(String originId) {
        telecomRepository.updateIsFilter(originId).subscribe();
        return Mono.just(new DataResponse<>(null, "success", null));
    }

    @Override
    public Mono<DataResponse> initFilterV2(String serviceAlias) {
        if (DataUtil.isNullOrEmpty(serviceAlias)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "serviceAlias.required"));
        }
        return telecomRepository
                .updateIsFilterByAlias(serviceAlias)
                .collectList()
                .flatMap(rs -> Mono.just(new DataResponse<>(null, "success", null)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getByOriginId(String originId, String serviceAlias) {
        return this.telecomRepository
                .getAllByOriginId(originId, serviceAlias)
                .collectList()
                .flatMap(telecoms -> Mono.just(new DataResponse<>("success", telecoms)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getByServiceAlias(String serviceAlias) {
        return this.telecomRepository
                .getAllByServiceAlias(serviceAlias)
                .collectList()
                .flatMap(telecoms -> Mono.just(new DataResponse<>("success", telecoms)));
    }

    @Override
    @LocalCache(autoCache = true, maxRecord = 10000)
    public Mono<DataResponse<PageResponse>> getPageTelecomService(PageTelecomRequest request) {
        Mono<Long> total = this.telecomRepositoryTemplate.getTotalByRequest(request);
        Mono<List<TelecomDTO>> telecoms =
                this.telecomRepositoryTemplate.getAllByRequest(request).collectList();
        return Mono.zip(total, telecoms)
                .flatMap(zip -> Mono.just(PageResponse.builder()
                        .total(zip.getT1())
                        .data(zip.getT2())
                        .build()))
                .flatMap(result -> Mono.just(new DataResponse<>("success", result)));
    }

    public Mono<DataResponse<List<String>>> getServiceTypes() {
        return telecomRepositoryTemplate
                .getServiceTypes()
                .collectList()
                .map(result -> new DataResponse<>("success", result));
    }

    @Override
    @LocalCache(autoCache = true, maxRecord = 10000)
    public Mono<List<TelecomResponse>> getAllTelecomServiceActive() {
        return Mono.zip(
                        telecomRepository.getAllTelecomServiceActive().collectList(),
                        marketInfoRepository.findAllMarketInfo().collectList())
                .flatMap(zip -> {
                    List<TelecomResponse> lstTelecomResponse = new ArrayList<>();
                    zip.getT1().forEach(telecom -> {
                        String marketImageUrl = null;
                        Optional<MarketInfo> marketInfo = zip.getT2().stream()
                                .filter(info -> DataUtil.safeEqual(info.getServiceId(), telecom.getOriginId()))
                                .findFirst();
                        if (marketInfo.isPresent()) {
                            marketImageUrl = marketInfo.get().getMarketImageUrl();
                        }
                        TelecomResponse e = TelecomResponse.builder()
                                .id(telecom.getId())
                                .name(telecom.getName())
                                .serviceAlias(telecom.getServiceAlias())
                                .description(telecom.getDescription())
                                .image(telecom.getImage())
                                .originId(telecom.getOriginId())
                                .status(telecom.getStatus())
                                .isFilter(telecom.getIsFilter())
                                .createBy(telecom.getCreateBy())
                                .updateBy(telecom.getUpdateBy())
                                .createAt(telecom.getCreateAt())
                                .updateAt(telecom.getUpdateAt())
                                .marketImageUrl(marketImageUrl)
                                .groupId(telecom.getGroupId())
                                .build();
                        lstTelecomResponse.add(e);
                    });
                    return Mono.just(lstTelecomResponse);
                })
                .doOnError(e -> Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "telecom.not.found")));
    }

    @Override
    public Mono<TelecomClient> getAdminRoleOfService(String originId) {
        return telecomRepository
                .findClientInfoByOriginId(originId)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(new BusinessException("error", "not.found")));
    }

    @Override
    public Mono<TelecomClient> getAdminRoleOfServiceByServiceAlias(String serviceAlias) {
        return telecomRepository
                .findClientInfoByServiceAlias(serviceAlias)
                .flatMap(Mono::just)
                .switchIfEmpty(Mono.error(new BusinessException("error", "not.found")));
    }

    @Override
    public Mono<DataResponse<List<TelecomServiceConfigDTO>>> getTelecomServiceConfig(
            List<String> telecomServiceIds, List<String> originalIds, String syncType) {
        if (DataUtil.isNullOrEmpty(syncType)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "syncType.not.null"));
        }
        if (telecomServiceIds != null) {
            return telecomServiceConfigRep
                    .getTelecomServiceConfig("$.".concat(syncType), telecomServiceIds)
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "config.not.found")))
                    .collectList()
                    .flatMap(rs -> Mono.just(new DataResponse<>("success", rs)));
        }
        if (originalIds != null) {
            return telecomServiceConfigRep
                    .getTelecomServiceConfig2("$.".concat(syncType), originalIds)
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "config.not.found")))
                    .collectList()
                    .flatMap(rs -> Mono.just(new DataResponse<>("success", rs)));
        }
        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "Dữ liệu truyền vào không hợp lệ"));
    }

    @Override
    public Mono<DataResponse<List<TelecomServiceConfigDTO>>> getTelecomServiceConfigV2(
            GetServiceConfigRequest request) {
        if (DataUtil.isNullOrEmpty(request.getSyncType())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "syncType.not.null"));
        }
        if (DataUtil.isNullOrEmpty(request.getLstServiceAlias())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "lstServiceAlis.not.null"));
        }
        return telecomServiceConfigRep
                .getTelecomServiceConfigByAlias("$.".concat(request.getSyncType()), request.getLstServiceAlias())
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "config.not.found")))
                .collectList()
                .flatMap(rs -> Mono.just(new DataResponse<>("success", rs)));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getAllTelecomServiceIdAndCode() {
        return telecomRepository
                .getAllTelecomService()
                .collectList()
                .map(telecoms -> new DataResponse<>("success", telecoms));
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getTelecomByLstOriginId(List<String> lstOriginId) {
        if (DataUtil.isNullOrEmpty(lstOriginId)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.list.origin.id.null"));
        }
        return telecomRepository
                .findTelecomByLstOriginId(lstOriginId)
                .collectList()
                .flatMap(telecoms -> Mono.just(new DataResponse<>("success", telecoms)));
    }

    @Override
    public Mono<ClientTelecom> getAliasByClientCode(String clientCode) {
        if (DataUtil.isNullOrEmpty(clientCode)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "setting.validate.client.code.null"));
        }
        return telecomRepository
                .checkExistClientCode(clientCode)
                .defaultIfEmpty("")
                .flatMap(cd -> {
                    if (DataUtil.isNullOrEmpty(cd)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.BAD_REQUEST, Translator.toLocaleVi("client.code.validate.null")));
                    }
                    return telecomRepository.getAliasByClientCode(clientCode);
                });
    }
}
