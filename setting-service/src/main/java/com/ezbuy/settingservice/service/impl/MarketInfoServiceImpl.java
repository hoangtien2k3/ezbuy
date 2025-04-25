package com.ezbuy.settingservice.service.impl;

import com.ezbuy.settingmodel.dto.MarketInfoDTO;
import com.ezbuy.settingmodel.dto.PaginationDTO;
import com.ezbuy.settingmodel.model.MarketInfo;
import com.ezbuy.settingmodel.request.MarketInfoRequest;
import com.ezbuy.settingmodel.request.SearchMarketInfoRequest;
import com.ezbuy.settingmodel.response.SearchMarketInfoResponse;
import com.ezbuy.settingservice.repository.MarketInfoRepository;
import com.ezbuy.settingservice.repositoryTemplate.MarketInfoRepositoryTemplate;
import com.ezbuy.settingservice.service.MarketInfoService;
import com.ezbuy.settingservice.service.TelecomService;
import com.reactify.config.MinioProperties;
import com.reactify.constants.CommonErrorCode;
import com.reactify.constants.Constants;
import com.reactify.exception.BusinessException;
import com.reactify.model.TokenUser;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.MinioUtils;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketInfoServiceImpl implements MarketInfoService {

    private final MinioUtils minioUtils;
    private final MinioProperties minioProperties;
    private final MarketInfoRepository marketInfoRepository;
    private final MarketInfoRepositoryTemplate marketInfoRepositoryTemplate;
    private final TelecomService telecomService;

    @Override
    @Transactional
    public Mono<DataResponse<SearchMarketInfoResponse>> searchMarketInfo(SearchMarketInfoRequest request) {
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
        return Mono.zip(
                        marketInfoRepositoryTemplate.queryList(request).collectList(),
                        marketInfoRepositoryTemplate.count(request))
                .flatMap(zip -> {
                    List<MarketInfoDTO> content = zip.getT1();
                    for (MarketInfoDTO item : content) {
                        item.setBase64(addPrefixImageBase64(
                                minioUtils.getBase64FromUrl(minioProperties.getBucket(), item.getMarketImageUrl()),
                                item.getMarketImageUrl()));
                    }
                    Long totalRecords = zip.getT2();

                    SearchMarketInfoResponse response = SearchMarketInfoResponse.builder()
                            .content(content)
                            .pagination(PaginationDTO.builder()
                                    .pageIndex(request.getPageIndex())
                                    .pageSize(request.getPageSize())
                                    .totalRecords(totalRecords)
                                    .build())
                            .build();
                    return Mono.just(response);
                })
                .map(res -> new DataResponse<>("success", res));
    }

    @Override
    @Transactional
    public Mono<DataResponse<List<MarketInfo>>> getAllMarketInfo() {
        return marketInfoRepository
                .findAllMarketInfo()
                .collectList()
                .map(marketInfoDTOS -> new DataResponse<>(Translator.toLocale("Success"), marketInfoDTOS));
    }

    // thi o man hinh chi tiet or viet
    // them function de load anh tu path ra web de hien thi
    @Override
    @Transactional
    public Mono<DataResponse<MarketInfo>> getMarketInfo(String id) {
        return marketInfoRepository
                .getById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.info.not.found")))
                .map(marketInfoDTO -> new DataResponse<>(Translator.toLocale("Success"), marketInfoDTO));
    }

    @Override
    @Transactional
    public Mono<DataResponse> createMarketInfo(MarketInfoRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Integer marketOrder = request.getMarketOrder();
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "market.info.not.found"))),
                        validateDuplicateMarketOrder(marketOrder),
                        validateDuplicateServiceId(request.getServiceId(), request.getServiceAlias()), // bo sung them
                        minioUtils.uploadMedia(request.getImage()))
                .flatMap(zip -> {
                    String imageLink = zip.getT4();
                    TokenUser tokenUser = zip.getT1();
                    String marketInfoId = UUID.randomUUID().toString();
                    MarketInfo marketInfo = MarketInfo.builder()
                            .id(marketInfoId)
                            .serviceId(request.getServiceId())
                            .title(request.getTitle())
                            .navigatorUrl(request.getNavigatorUrl())
                            .marketOrder(request.getMarketOrder())
                            .marketImageUrl(imageLink)
                            .status(Constants.Activation.ACTIVE)
                            .createBy(tokenUser.getUsername())
                            .serviceAlias(request.getServiceAlias()) // alias of service PYCXXX/LuongToanTrinhScontract
                            .createAt(now)
                            .build();
                    marketInfo.setId(marketInfoId);
                    return marketInfoRepository
                            .save(marketInfo)
                            .onErrorReturn(new MarketInfo())
                            .flatMap(result -> Mono.just(new DataResponse<>("success", result)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<MarketInfo>> updateMarketInfo(String id, MarketInfoRequest request) {
        if (DataUtil.isNullOrEmpty(id)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "market.info.id.not.empty");
        }
        return Mono.zip(
                        SecurityUtils.getCurrentUser()
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "market.info.not.found"))),
                        minioUtils.uploadMedia(request.getImage()),
                        marketInfoRepository.getById(id))
                .flatMap(zip -> {
                    TokenUser tokenUser = zip.getT1();
                    String imageUrl = zip.getT2();
                    MarketInfo marketInfo = zip.getT3();
                    String marketInfoAlias = marketInfo.getServiceAlias(); // alias cua marketInfo
                    // PYCXXX/LuongToanTrinhScontract
                    Mono<Boolean> checkExistMarketOrder = Mono.just(true);
                    Mono<Boolean> checkExistServiceId = Mono.just(true);
                    if (!DataUtil.safeEqual(request.getMarketOrder(), marketInfo.getMarketOrder())) {
                        checkExistMarketOrder = validateDuplicateMarketOrder(request.getMarketOrder());
                    }
                    if (!DataUtil.safeEqual(request.getServiceId(), marketInfo.getServiceId())) {
                        checkExistServiceId = validateDuplicateServiceId(
                                request.getServiceId(),
                                request.getServiceAlias()); // bo sung them serviceAlias ko anh huong den luong cu neu
                        // serviceAlias null
                    }

                    // neu serviceAlias trong request khac null thi gan marketInfoAlias trong co so
                    // du lieu vao
                    // serviceAlias
                    if (!DataUtil.isNullOrEmpty(request.getServiceAlias())) {
                        marketInfoAlias = request.getServiceAlias();
                    }

                    Mono<MarketInfo> updateMarketInfoMono = marketInfoRepository
                            .updateMarketInfo(
                                    request.getServiceId(),
                                    request.getTitle(),
                                    request.getNavigatorUrl(),
                                    request.getMarketOrder(),
                                    imageUrl,
                                    request.getStatus(),
                                    tokenUser.getUsername(),
                                    id,
                                    marketInfoAlias)
                            .defaultIfEmpty(new MarketInfo());
                    return Mono.zip(checkExistServiceId, checkExistMarketOrder, updateMarketInfoMono)
                            .flatMap(response -> Mono.just(new DataResponse<>("success", null)));
                })
                .map(rs -> new DataResponse<>("success", null));
    }

    @Override
    public Mono<List<MarketInfo>> getAllActiveMarketInfo() {
        return marketInfoRepository.getAllActiveMarketInfo().collectList().map(list -> list);
    }

    private Mono<Boolean> validateDuplicateMarketOrder(Integer marketOrder) {
        return marketInfoRepository
                .findByMarketOrder(marketOrder)
                .defaultIfEmpty(new MarketInfo())
                .flatMap(marketInfo -> {
                    if (marketInfo.getId() != null) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.BAD_REQUEST,
                                Translator.toLocaleVi("market.info.telecom.market.order.existed", marketOrder)));
                    }
                    return Mono.just(true);
                });
    }

    private void validateFileSize(byte[] bytes) {
        int fileSizeMB = bytes.length / (1024 * 1024);
        if (fileSizeMB >= 10) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "upload.size.limit");
        }
    }

    private Mono<Boolean> validateDuplicateServiceId(String telecomServiceId, String serviceAlias) {
        // bo sung tim kiem theo serviceAlias
        // neu serviceAlias null thi van truy van theo telecomServiceId binh thuong
        return marketInfoRepository
                .findByServiceId(telecomServiceId, serviceAlias)
                .collectList()
                .flatMap(marketInfoDTOS -> {
                    if (!marketInfoDTOS.isEmpty()) {
                        // bo sung tim kiem theo serviceAlias
                        return telecomService
                                .getByOriginId(telecomServiceId, serviceAlias)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi("market.info.telecom.service.id"))))
                                .flatMap(result -> {
                                    // fix bug khi result co data null
                                    String resultFinal = serviceAlias;
                                    if (!DataUtil.isNullOrEmpty(result.getData())) {
                                        resultFinal =
                                                result.getData().getFirst().getName();
                                    }
                                    return Mono.error(new BusinessException(
                                            CommonErrorCode.BAD_REQUEST,
                                            Translator.toLocaleVi(
                                                    "market.info.telecom.service.id.existed", resultFinal)));
                                });
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> validateDuplicateServiceAlias(String serviceAlias) {
        return marketInfoRepository
                .findByServiceAlias(serviceAlias)
                .collectList()
                .flatMap(marketInfoDTOS -> {
                    if (!marketInfoDTOS.isEmpty()) {
                        return telecomService
                                .getByServiceAlias(serviceAlias)
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi("market.info.telecom.service.id"))))
                                .flatMap(result -> Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi(
                                                "market.info.telecom.service.id.existed",
                                                result.getData().getFirst().getName()))));
                    }
                    return Mono.just(true);
                });
    }

    private String addPrefixImageBase64(String base64, String url) {
        // anh cua mbccs can phai format lai data
        String[] lstUrl = url.split("\\/");
        String nameImage = lstUrl[lstUrl.length - 1];
        String extension = FilenameUtils.getExtension(nameImage); // lay duoi cua anh
        return DataUtil.isNullOrEmpty(extension)
                ? ""
                : "data:image/" + extension.trim() + ";base64," + base64; // chuyen
        // sang
        // dang;
    }

    @Override
    public Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceId(List<String> lstServiceId) {
        return marketInfoRepository
                .getByServiceId(lstServiceId)
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.info.not.found")))
                .map(marketInfoDTO -> new DataResponse<>(Translator.toLocale("Success"), marketInfoDTO));
    }

    @Override
    public Mono<DataResponse<List<MarketInfo>>> getMarketInfoByServiceIdV2(List<String> lstAlias) {
        return marketInfoRepository
                .getByServiceAlias(lstAlias)
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "market.info.not.found")))
                .map(marketInfoDTO -> new DataResponse<>(Translator.toLocale("Success"), marketInfoDTO));
    }
}
