package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.productmodel.dto.FilterProductTemplateDTO;
import com.ezbuy.productmodel.request.ApiUtils;
import com.ezbuy.productmodel.response.ListProductOfferResponse;
import com.ezbuy.productmodel.response.ProductOfferTemplateDTO;
import com.ezbuy.productservice.client.ProductClient;
import com.ezbuy.productservice.client.SettingClient;
import com.ezbuy.productservice.repository.repoTemplate.TelecomServiceRepository;
import com.ezbuy.productservice.service.ProductOfferTemplateService;
import java.util.List;

import com.reactify.annotations.LocalCache;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductOfferTemplateServiceImpl implements ProductOfferTemplateService {

    private final ProductClient productClient;
    private final SettingClient settingClient;
    private final TelecomServiceRepository telecomServiceRepository;

    @Override
    @LocalCache(durationInMinute = 30)
    public Mono<DataResponse> getProductTemplate(FilterProductTemplateDTO filterProductTemplateDTO) {
        if (!DataUtil.isNullOrEmpty(filterProductTemplateDTO.getListId())) {
            return getProductInfoByIds(filterProductTemplateDTO.getListId());
        } else {
            if (DataUtil.isNullOrEmpty(filterProductTemplateDTO.getTelecomServiceAlias())) {
                return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "telecomServiceAlias.required"));
            }
            if (!DataUtil.isNullOrEmpty(filterProductTemplateDTO.getUtils())) {
                if (DataUtil.isNullOrEmpty(filterProductTemplateDTO.getUtils().getPageSize())) {
                    filterProductTemplateDTO.getUtils().setPageSize(20);
                } else if (filterProductTemplateDTO.getUtils().getPageSize() > 50) {

                    return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "pageSize.error"));
                }
            }
            return settingClient
                    .getTelecomService(filterProductTemplateDTO.getTelecomServiceAlias())
                    .flatMap(listTelecomService -> {
                        if (DataUtil.isNullOrEmpty(listTelecomService)) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.BAD_REQUEST, "telecomServiceAlias.error"));
                        }
                        if (listTelecomService.stream().noneMatch(x -> x.getServiceAlias()
                                .equalsIgnoreCase(
                                        DataUtil.safeTrim(filterProductTemplateDTO.getTelecomServiceAlias())))) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.BAD_REQUEST, "telecomServiceAlias.error"));
                        }
                        return getLstTemplateOfferByFilters(
                                listTelecomService.stream()
                                        .filter(x -> x.getServiceAlias()
                                                .equalsIgnoreCase(DataUtil.safeTrim(
                                                        filterProductTemplateDTO.getTelecomServiceAlias())))
                                        .findFirst()
                                        .get()
                                        .getOriginId(),
                                filterProductTemplateDTO.getUtils(),
                                filterProductTemplateDTO.getPriceTypes(),
                                filterProductTemplateDTO.getTelecomServiceAlias());
                    });
        }
    }

    @Override
    public Mono<DataResponse> getProductsForMegaMenu() {
        return telecomServiceRepository
                .getProductsForMegaMenu()
                .collectList()
                .map((item) -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), item));
    }

    private Mono<DataResponse> getProductInfoByIds(List<String> ids) {
        return productClient.getListProductOfferTemplateByListIds(ids).map(respOptional -> {
            if (respOptional.isEmpty()
                    || DataUtil.isNullOrEmpty(respOptional.get().getData())) {
                return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null);
            }
            ListProductOfferResponse response = respOptional.get();
            response.getData().forEach(ProductOfferTemplateDTO::handleDataGetByIds);
            return new DataResponse<>(Translator.toLocaleVi(SUCCESS), response);
        });
    }

    private Mono<DataResponse> getLstTemplateOfferByFilters(
            String telecomServiceId, ApiUtils utils, List<String> priceTypes, String telecomServiceAlias) {
        ProductOfferTemplateDTO productOfferTemplateDTO = new ProductOfferTemplateDTO();
        // danh gia khong can sua vi ham nay phia PRODUCT dang chi filter theo
        // telecomServiceId
        return productClient
                .getLstTemplateOffer(telecomServiceId, utils, priceTypes)
                .map(respOptional -> {
                    if (respOptional.isEmpty()
                            || DataUtil.isNullOrEmpty(respOptional.get().getData())) {
                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), null);
                    }
                    ListProductOfferResponse response = respOptional.get();
                    response.getData().forEach(ProductOfferTemplateDTO::handleDataGetByIds);
                    response.getData().forEach(element -> {
                        element.setTelecomServiceAlias(telecomServiceAlias);
                    });
                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), response);
                });
    }
}
