package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.authmodel.constants.AuthConstants;
import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.dto.GetServiceConnectDTO;
import com.ezbuy.productmodel.dto.ProductSpecCharAndValDTO;
import com.ezbuy.productmodel.dto.ProductSpecCharValueDTO;
import com.ezbuy.productmodel.dto.UpdateAccountServiceInfoDTO;
import com.ezbuy.productmodel.dto.ws.SubscriberCMResponse;
import com.ezbuy.productmodel.model.ActiveTelecom;
import com.ezbuy.productmodel.model.ProductSpecChar;
import com.ezbuy.productmodel.model.ProductSpecCharValue;
import com.ezbuy.productmodel.model.Subscriber;
import com.ezbuy.productmodel.model.Telecom;
import com.ezbuy.productmodel.request.FilterCreatingRequest;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActive;
import com.ezbuy.productmodel.request.FilterGetListSubscriberActiveByAlias;
import com.ezbuy.productmodel.request.GetListSubscriberActive;
import com.ezbuy.productmodel.response.LstServiceCharacteristicResponse;
import com.ezbuy.productmodel.response.ServiceCharacteristicDTO;
import com.ezbuy.productservice.client.AuthClient;
import com.ezbuy.productservice.client.CmClient;
import com.ezbuy.productservice.client.ProductClient;
import com.ezbuy.productservice.client.SettingClient;
import com.ezbuy.productservice.repository.*;
import com.ezbuy.productservice.repository.repoTemplate.ProductSpecRepo;
import com.ezbuy.productservice.service.ProductSpecService;
import com.ezbuy.productservice.service.RenewalCAService;
import com.ezbuy.productservice.utils.DataUtils;
import com.ezbuy.settingmodel.dto.TelecomDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.factory.ModelMapperFactory;
import com.reactify.factory.ObjectMapperFactory;
import com.reactify.model.response.DataResponse;
import com.reactify.util.AppUtils;
import com.reactify.util.DataUtil;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSpecServiceImpl implements ProductSpecService {

    private final ProductSpecCharRepository productSpecCharRepository;
    private final ProductSpecCharValueRepository productSpecCharValueRepository;
    private final ProductSpecRepo productSpecRepo;
    private final SubscriberRepository subscriberRepository;
    private final SettingClient settingClient;
    private final ProductClient productClient;
    private final RenewalCAService renewalCAService;
    private final AuthClient authClient;
    private final ServiceRelationshipRepository serviceRelationshipRepository;
    private final TelecomRepository telecomRepository;
    private final ActiveTelecomRepository activeTelecomRepository;
    private final CmClient cmClient;

    /**
     * get list filter for product landing page
     *
     * @param telecomServiceAlias
     * @return
     */
    @Override
    public Mono<DataResponse> getFilterTemplate(String telecomServiceAlias) {
        if (DataUtil.isNullOrEmpty(telecomServiceAlias)) {
            log.error("Got null param");
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "telecomServiceAlias.required"));
        }
        telecomServiceAlias = DataUtil.safeTrim(telecomServiceAlias);
        return productSpecRepo
                .findByTelecomServiceAliasIncludeValue(telecomServiceAlias)
                .collectList()
                .map(list -> {
                    Map<UUID, ProductSpecCharAndValDTO> mapTmp = new HashMap<>();
                    list.forEach(x -> {
                        if (!mapTmp.containsKey(DataUtil.safeToUUID(x.getId()))) {
                            ProductSpecCharAndValDTO productSpecCharAndValDTO = new ProductSpecCharAndValDTO();
                            ModelMapperFactory.getInstance().map(x, productSpecCharAndValDTO);
                            mapTmp.put(DataUtil.safeToUUID(x.getId()), productSpecCharAndValDTO);
                        }
                    });
                    list.forEach(y -> {
                        ProductSpecCharAndValDTO productSpecCharAndValDTO = mapTmp.get(y.getValProductSpecCharId());
                        ProductSpecCharValueDTO productSpecCharValueDTO = new ProductSpecCharValueDTO(
                                y.getValId().toString(),
                                y.getValProductSpecCharId().toString(),
                                y.getValValue(),
                                y.getValName(),
                                y.getValOrderDisplay(),
                                y.getValState(),
                                y.getStatus());
                        if (DataUtil.isNullOrEmpty(productSpecCharAndValDTO.getProductSpecCharValueDTOList())) {
                            productSpecCharAndValDTO.setProductSpecCharValueDTOList(new ArrayList<>());
                        }
                        productSpecCharAndValDTO
                                .getProductSpecCharValueDTOList()
                                .add(productSpecCharValueDTO);
                    });
                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), mapTmp.values());
                });
    }

    @Override
    public Mono<DataResponse> syncFilterTemplate() {
        // get full telecomService Info from setting service
        return settingClient
                .getTelecomService(null)
                .flatMap(listTelecomService -> {
                    if (DataUtil.isNullOrEmpty(listTelecomService)) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "telecomService.empty"));
                    }

                    // call ws from product system to get filter data
                    List<Mono<LstServiceCharacteristicResponse>> serviceCharacterMonoList = listTelecomService.stream()
                            .map(telecom -> getServiceCharacteristic(telecom.getOriginId()))
                            .collect(Collectors.toList());
                    return Mono.zip(serviceCharacterMonoList, response -> Arrays.stream(response)
                                    .map(object -> (LstServiceCharacteristicResponse) object)
                                    .collect(Collectors.toList()))
                            .flatMap(serviceCharacterList -> {
                                var productSpecCharMonoList = findAllActiveProductSpec();
                                return Mono.zip(
                                        Mono.just(serviceCharacterList),
                                        productSpecCharMonoList,
                                        Mono.just(listTelecomService));
                            });
                })
                .flatMap(productSpecGroupData -> {
                    List<LstServiceCharacteristicResponse> serviceCharacteristicResponse = productSpecGroupData.getT1();
                    List<ProductSpecCharAndValDTO> productSpecList = productSpecGroupData.getT2();
                    List<TelecomDTO> telecomList = productSpecGroupData.getT3();
                    List<ProductSpecCharAndValDTO> updateProductSpecList = new ArrayList<>();

                    List<ServiceCharacteristicDTO> insertServiceCharacteristicList = new ArrayList<>();
                    List<ProductSpecCharAndValDTO> deleteSpecList = new ArrayList<>();

                    List<ProductSpecCharValueDTO> deleteProductSpecValueList = new ArrayList<>();
                    List<ProductSpecCharValue> insertProductSpecValueList = new ArrayList<>();

                    for (LstServiceCharacteristicResponse serviceCharacteristicResp : serviceCharacteristicResponse) {
                        if (DataUtil.isNullOrEmpty(serviceCharacteristicResp.getData())) {
                            Optional<ProductSpecCharAndValDTO> deleteProductSpecOptional = productSpecList.stream()
                                    .filter(spec -> DataUtil.safeEqual(
                                                    spec.getTelecomServiceId(),
                                                    serviceCharacteristicResp.getTelecomServiceId())
                                            && !DataUtil.safeEqual(spec.getCode(), Constants.PRICE_FILTER))
                                    .findAny();
                            deleteProductSpecOptional.ifPresent(deleteSpecList::add);
                            continue;
                        }
                        for (ServiceCharacteristicDTO serviceCharacteristic : serviceCharacteristicResp.getData()) {
                            String code = DataUtil.safeToString(serviceCharacteristic.getCode());
                            ProductSpecCharAndValDTO productSpec = getAndRemoveProductSpec(
                                    serviceCharacteristicResp.getTelecomServiceId(), code, productSpecList);

                            Optional<TelecomDTO> telecomOptional = telecomList.stream()
                                    .filter(telecom -> DataUtil.safeEqual(
                                            serviceCharacteristic.getTelecomServiceId(), telecom.getOriginId()))
                                    .findAny();
                            String telecomAlias = telecomOptional.isPresent()
                                    ? telecomOptional.get().getServiceAlias()
                                    : "";

                            if (productSpec == null) {
                                serviceCharacteristic.setTelecomServiceId(
                                        serviceCharacteristicResp.getTelecomServiceId());
                                serviceCharacteristic.setTelecomServiceAlias(telecomAlias);
                                insertServiceCharacteristicList.add(serviceCharacteristic);
                                continue;
                            }
                            List<ProductSpecCharValueDTO> productSpecCharValueList =
                                    productSpec.getProductSpecCharValueDTOList();
                            List<com.ezbuy.productmodel.response.ProductSpecCharValueDTO> values =
                                    serviceCharacteristic.getProductSpecCharValueDTOList() != null
                                            ? serviceCharacteristic.getProductSpecCharValueDTOList()
                                            : new ArrayList<>();
                            List<ProductSpecCharValueDTO> tempDeleteProductSpecValueList =
                                    productSpecCharValueList.stream()
                                            .filter(specValue -> values.stream()
                                                    .noneMatch(value ->
                                                            DataUtil.safeEqual(value.getValue(), specValue.getValue())))
                                            .toList();
                            deleteProductSpecValueList.addAll(tempDeleteProductSpecValueList);

                            List<ProductSpecCharValue> tempInsertProductSpecValueList = values.stream()
                                    .filter(value -> productSpecCharValueList.stream()
                                            .noneMatch(specValue ->
                                                    DataUtil.safeEqual(specValue.getValue(), value.getValue())))
                                    .map(value -> ProductSpecCharValue.builder()
                                            .id(UUID.randomUUID().toString())
                                            .productSpecCharId(productSpec.getId())
                                            .name(value.getName())
                                            .value(value.getValue())
                                            .status(1)
                                            .state(0)
                                            .build())
                                    .toList();
                            insertProductSpecValueList.addAll(tempInsertProductSpecValueList);

                            productSpec.setName(serviceCharacteristic.getName());
                            productSpec.setTelecomServiceAlias(telecomAlias);
                            List<ProductSpecCharValueDTO> updateSpecCharValueList = new ArrayList<>();
                            for (ProductSpecCharValueDTO productSpecValue : productSpecCharValueList) {
                                Optional<com.ezbuy.productmodel.response.ProductSpecCharValueDTO>
                                        productSpecCharValueOptional = values.stream()
                                                .filter(value -> DataUtil.safeEqual(
                                                        value.getValue(), productSpecValue.getValue()))
                                                .findAny();
                                productSpecCharValueOptional.ifPresent(productSpecCharValueDTO -> {
                                    productSpecValue.setName(productSpecCharValueDTO.getName());
                                    updateSpecCharValueList.add(productSpecValue);
                                });
                            }
                            productSpec.setProductSpecCharValueDTOList(updateSpecCharValueList);
                            updateProductSpecList.add(productSpec);
                        }
                    }

                    var productSpecCharValueListMono = insertServiceCharacteristicList(insertServiceCharacteristicList);
                    if (!DataUtil.isNullOrEmpty(productSpecList)) {
                        productSpecList = productSpecList.stream()
                                .filter(p -> !DataUtil.safeEqual(p.getCode(), Constants.PRICE_FILTER))
                                .collect(Collectors.toList());
                    }
                    deleteSpecList.addAll(productSpecList);
                    var updateProductSpecCharListMono = updateProductSpecCharList(updateProductSpecList);
                    var deleteProductSpecCharListMono = deleteProductSpecCharList(deleteSpecList);
                    var insertProductSpecValueListMono = AppUtils.insertData(productSpecCharValueRepository
                            .saveAll(insertProductSpecValueList)
                            .collectList());
                    var deleteProductSpecValueListMono =
                            AppUtils.insertData(deleteProductSpecCharValueList(deleteProductSpecValueList));
                    return Mono.zip(
                                    productSpecCharValueListMono,
                                    updateProductSpecCharListMono,
                                    deleteProductSpecCharListMono,
                                    insertProductSpecValueListMono,
                                    deleteProductSpecValueListMono)
                            .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                });
    }

    @Override
    public Mono<DataResponse> getFilterDetail(String telecomServiceId, Boolean isPreview) {
        return productSpecCharRepository
                .findByTelecomId(telecomServiceId, isPreview)
                .collectList()
                .flatMap(p -> getValues(p, isPreview));
    }

    @Override
    @Transactional
    public Mono<DataResponse> createFilter(FilterCreatingRequest request) {
        return settingClient
                .getTelecomServiceByOriginId(request.getTelecomServiceId())
                .flatMap(telecoms -> {
                    if (DataUtil.isNullOrEmpty(telecoms)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.BAD_REQUEST, "telecom-service.not.found"));
                    }
                    TelecomDTO telecomDTO = telecoms.get(0);
                    return getFilterDetail(request.getTelecomServiceId(), null).flatMap(p -> {
                        validateRequestData(request);
                        List<ProductSpecCharAndValDTO> oldProducts = (List<ProductSpecCharAndValDTO>) p.getData();
                        List<ProductSpecCharAndValDTO> updatedProductSpecChars =
                                getUpdatedProduct(oldProducts, request);
                        List<ProductSpecCharAndValDTO> newProductSpecChars = getNewProductSpecChar(request, telecomDTO);
                        if (Objects.equals(telecomDTO.getIsFilter(), Boolean.TRUE)) {
                            if (!DataUtil.isNullOrEmpty(newProductSpecChars)) {
                                return Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST,
                                        Translator.toLocaleVi("edit-filter.not.allow.new")));
                            }
                        }
                        List<ProductSpecCharValue> newSpecCharValues = new ArrayList<>();
                        List<ProductSpecCharValueDTO> updatedSpecCharValues = new ArrayList<>();
                        List<ProductSpecCharValueDTO> deletedSpecCharValues = new ArrayList<>();
                        for (ProductSpecCharAndValDTO newProduct : request.getProductSpecCharList()) {
                            ProductSpecCharAndValDTO oldProduct = null;
                            if (Objects.nonNull(newProduct.getId())) {
                                oldProduct = oldProducts.stream()
                                        .filter(pro -> Objects.equals(pro.getId(), newProduct.getId()))
                                        .findFirst()
                                        .orElse(null);
                            }
                            newSpecCharValues.addAll(getNewSpecCharValues(newProduct));
                            if (oldProduct != null) {
                                updatedSpecCharValues.addAll(getUpdatedSpecCharValues(newProduct, oldProduct));
                                deletedSpecCharValues.addAll(getDeletedSpecCharValue(newProduct, oldProduct));
                            }
                        }
                        var insertProductMono = AppUtils.insertData(createProductSpecChars(newProductSpecChars));
                        var updatedProductMono = AppUtils.insertData(productSpecRepo
                                .updateProductSpecChar(updatedProductSpecChars)
                                .collectList());
                        var newProductSpecCharValueMono = AppUtils.insertData(productSpecCharValueRepository
                                .saveAll(newSpecCharValues)
                                .collectList());
                        var updatedProductSpecCharValueMono = AppUtils.insertData(productSpecRepo
                                .updateProductSpecCharValue(updatedSpecCharValues)
                                .collectList());
                        var deletedProductSpecCharValueMono =
                                AppUtils.insertData(deleteProductSpecCharValueList(deletedSpecCharValues));
                        Mono<DataResponse<Object>> telecomServiceMono;
                        if (!Objects.equals(telecomDTO.getIsFilter(), Boolean.TRUE)) {
                            telecomServiceMono = settingClient
                                    .updateIsFilter(request.getTelecomServiceId())
                                    .onErrorResume(throwable -> {
                                        log.error("call.api.setting.error");
                                        return Mono.error(new BusinessException(
                                                CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                Translator.toLocaleVi("call.api.setting.error")));
                                    });
                        } else {
                            telecomServiceMono = Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                        }
                        return Mono.zip(
                                        insertProductMono,
                                        updatedProductMono,
                                        newProductSpecCharValueMono,
                                        updatedProductSpecCharValueMono,
                                        deletedProductSpecCharValueMono,
                                        telecomServiceMono)
                                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    });
                });
    }

    public Mono<Boolean> validateRequestDataNew(ProductSpecCharAndValDTO request) {
        return productSpecCharRepository
                .listCodeSpectCharValue(request.getTelecomServiceId())
                .collectList()
                .flatMap(codeAndIdList -> {
                    Map<String, String> codeIdMap = codeAndIdList.stream()
                            .collect(Collectors.toMap(
                                    ProductSpecCharAndValDTO::getCode,
                                    ProductSpecCharAndValDTO::getId,
                                    (existing, replacement) -> existing));
                    Map<Integer, String> displayOrderMap = codeAndIdList.stream()
                            .collect(Collectors.toMap(
                                    ProductSpecCharAndValDTO::getDisplayOrder,
                                    ProductSpecCharAndValDTO::getId,
                                    (existing, replacement) -> existing));
                    // Kiểm tra điều kiện và ném lỗi nếu cần
                    if (DataUtil.isNullOrEmpty(request.getId())) {
                        if (DataUtil.isNullOrEmpty(request.getCode())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "product-spec-char.code.empty"));
                        }
                        if (DataUtil.isNullOrEmpty(request.getName())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "product-spec-char.name.empty"));
                        }
                        if (codeIdMap.containsKey(request.getCode())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "product-spec-char.code.duplicate"));
                        }
                    } else {
                        String existingId = codeIdMap.get(request.getCode());
                        if (existingId != null && !existingId.equals(request.getId())) {
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "product-spec-char.code.duplicate"));
                        }
                    }

                    if (DataUtil.isNullOrEmpty(request.getDisplayOrder()) || request.getDisplayOrder() < 1) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char.display-order.null"));
                    }
                    if (displayOrderMap.containsKey(request.getDisplayOrder())) {
                        // Kiểm tra xem ID của displayOrder có khớp với ID hiện tại không
                        String existingId = displayOrderMap.get(request.getDisplayOrder());
                        if (existingId != null && !existingId.equals(request.getId())) {
                            // Nếu display order đã tồn tại và không phải là cùng một ID, ném lỗi
                            return Mono.error(new BusinessException(
                                    CommonErrorCode.INVALID_PARAMS, "product-spec-char.display-order.duplicate"));
                        }
                    }

                    if (DataUtil.isNullOrEmpty(request.getViewType())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-view-type.null"));
                    }

                    if (DataUtil.isNullOrEmpty(request.getStatus())) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.status.null"));
                    }

                    // Gọi validateChildDataNew một cách phản ứng
                    return validateChildDataNew(request);
                });
    }

    private Mono<Boolean> validateChildDataNew(ProductSpecCharAndValDTO product) {
        Set<String> uniqueValues = new HashSet<>();
        boolean allStatusZero = product.getProductSpecCharValueDTOList().stream()
                .allMatch(value -> Integer.valueOf(0).equals(value.getStatus()));
        if (allStatusZero) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "the-filter-must-have-at-least-1-criterion"));
        }

        // Kiểm tra child data
        if (!DataUtil.isNullOrEmpty(product.getProductSpecCharValueDTOList())) {
            for (ProductSpecCharValueDTO value : product.getProductSpecCharValueDTOList()) {
                if (Objects.equals(product.getViewType(), "RANGE")) {
                    // Lay ra truong value co displayOrder min
                    ProductSpecCharValueDTO minDisplayOrder = product.getProductSpecCharValueDTOList().stream()
                            .min(Comparator.comparing(ProductSpecCharValueDTO::getDisplayOrder))
                            .get();
                    // lay ra truong value co displayOrder max
                    ProductSpecCharValueDTO maxDisplayOrder = product.getProductSpecCharValueDTOList().stream()
                            .max(Comparator.comparing(ProductSpecCharValueDTO::getDisplayOrder))
                            .get();
                    int minValue = Integer.parseInt(minDisplayOrder.getValue());
                    int maxValue = Integer.parseInt(maxDisplayOrder.getValue());
                    if (minValue > maxValue) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "minValue-invalid"));
                    }
                }
                // Kiểm tra điều kiện và ném lỗi nếu cần
                if (value == null) {
                    return Mono.error(
                            new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char-value-empty"));
                }
                if (DataUtil.isNullOrEmpty(value.getId())) {
                    if (DataUtil.isNullOrEmpty(value.getValue())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value-value-empty"));
                    }
                    if (DataUtil.isNullOrEmpty(value.getName())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value-name-empty"));
                    }
                    if (DataUtil.isNullOrEmpty(value.getDisplayOrder())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value-display-empty"));
                    }
                    if (DataUtil.isNullOrEmpty(value.getStatus())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value-status-empty"));
                    }
                }
                if (DataUtil.isNullOrEmpty(value.getStatus())) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.status.null"));
                }
                if (DataUtil.isNullOrEmpty(value.getDisplayOrder()) || value.getDisplayOrder() < 1) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.display-order.null"));
                }
                if (!uniqueValues.add(value.getValue())) {
                    return Mono.error(new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.value.duplicate"));
                }
            }

            long nonDuplicatedNum = product.getProductSpecCharValueDTOList().stream()
                    .map(ProductSpecCharValueDTO::getDisplayOrder)
                    .distinct()
                    .count();
            if (nonDuplicatedNum != product.getProductSpecCharValueDTOList().size()) {
                return Mono.error(new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.display-order.duplicate"));
            }
        }
        return Mono.just(true); // Nếu không có lỗi
    }

    private void validateRequestData(FilterCreatingRequest request) {
        for (ProductSpecCharAndValDTO product : request.getProductSpecCharList()) {
            if (DataUtil.isNullOrEmpty(product.getId())) {
                if (DataUtil.isNullOrEmpty(product.getCode())) {
                    throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.code.empty");
                }
                if (DataUtil.isNullOrEmpty(product.getName())) {
                    throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.name.empty");
                }
            }
            if (DataUtil.isNullOrEmpty(product.getDisplayOrder()) || product.getDisplayOrder() < 1) {
                throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.display-order.null");
            }
            validateChildData(product);
        }
        long nonDuplicatedNum = request.getProductSpecCharList().stream()
                .map(ProductSpecCharAndValDTO::getDisplayOrder)
                .distinct()
                .count();
        if (nonDuplicatedNum != request.getProductSpecCharList().size()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.display-order.duplicate");
        }
    }

    private void validateChildData(ProductSpecCharAndValDTO product) {
        if (!DataUtil.isNullOrEmpty(product.getProductSpecCharValueDTOList())) {
            for (ProductSpecCharValueDTO value : product.getProductSpecCharValueDTOList()) {
                if (DataUtil.isNullOrEmpty(value.getId())) {
                    if (DataUtil.isNullOrEmpty(value.getValue())) {
                        throw new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.value.empty");
                    }
                    if (DataUtil.isNullOrEmpty(value.getName())) {
                        throw new BusinessException(
                                CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.name.empty");
                    }
                }
                if (DataUtil.isNullOrEmpty(value.getState()) || value.getState() < 0 || value.getState() > 1) {
                    throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.state.null");
                }
                if (DataUtil.isNullOrEmpty(value.getDisplayOrder()) || value.getDisplayOrder() < 1) {
                    throw new BusinessException(
                            CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.display-order.null");
                }
            }

            long nonDuplicatedNum = product.getProductSpecCharValueDTOList().stream()
                    .map(ProductSpecCharValueDTO::getDisplayOrder)
                    .distinct()
                    .count();
            if (nonDuplicatedNum != product.getProductSpecCharValueDTOList().size()) {
                throw new BusinessException(
                        CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.display-order.duplicate");
            }
        }
    }

    private Mono<Boolean> createProductSpecChars(List<ProductSpecCharAndValDTO> productSpecChars) {
        if (!DataUtil.isNullOrEmpty(productSpecChars)) {
            List<ProductSpecChar> newProducts = new ArrayList<>();
            ObjectMapper mapper = ObjectMapperFactory.getInstance();
            for (ProductSpecCharAndValDTO pro : productSpecChars) {
                ProductSpecChar productSpecChar = mapper.convertValue(pro, ProductSpecChar.class);
                productSpecChar.setDisplayOrder(pro.getDisplayOrder());
                productSpecChar.setStatus(pro.getStatus());
                productSpecChar.setNew(true);
                productSpecChar.setViewType(pro.getViewType());
                productSpecChar.setState(1);
                newProducts.add(productSpecChar);
            }
            return productSpecCharRepository.saveAll(newProducts).collectList().map(result -> true);
        }
        return Mono.just(Boolean.TRUE);
    }

    private List<ProductSpecCharAndValDTO> getNewProductSpecChar(FilterCreatingRequest request, TelecomDTO telecomDTO) {
        List<ProductSpecCharAndValDTO> newProductSpecChars = request.getProductSpecCharList().stream()
                .filter(p -> Objects.isNull(p.getId()))
                .collect(Collectors.toList());
        for (ProductSpecCharAndValDTO pro : newProductSpecChars) {
            pro.setTelecomServiceId(request.getTelecomServiceId());
            pro.setTelecomServiceAlias(
                    DataUtil.isNullOrEmpty(telecomDTO.getServiceAlias()) ? "" : telecomDTO.getServiceAlias());
        }
        return newProductSpecChars;
    }

    private List<ProductSpecCharAndValDTO> getNewProductSpecCharNew(
            ProductSpecCharAndValDTO request, TelecomDTO telecomDTO) {
        List<ProductSpecCharAndValDTO> newProductSpecChars = new ArrayList<>();
        if (DataUtil.isNullOrEmpty(request.getId())) {
            // Nếu là null, thêm request vào danh sách newProductSpecChars
            // Thực hiện cập nhật thông tin cho request
            request.setTelecomServiceId(request.getTelecomServiceId());
            request.setTelecomServiceAlias(
                    DataUtil.isNullOrEmpty(telecomDTO.getServiceAlias()) ? "" : telecomDTO.getServiceAlias());

            newProductSpecChars.add(request);
        }
        return newProductSpecChars;
    }

    private List<ProductSpecCharAndValDTO> getUpdatedProduct(
            List<ProductSpecCharAndValDTO> oldProducts, FilterCreatingRequest request) {
        List<ProductSpecCharAndValDTO> updatedProducts = request.getProductSpecCharList().stream()
                .filter(p -> Objects.nonNull(p.getId()))
                .collect(Collectors.toList());
        if (oldProducts.size() != updatedProducts.size()) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.must.edit");
        }
        Set<String> oldIds =
                oldProducts.stream().map(ProductSpecCharAndValDTO::getId).collect(Collectors.toSet());
        Set<String> newIds = updatedProducts.stream()
                .map(ProductSpecCharAndValDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (!oldIds.containsAll(newIds)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.non.match");
        }

        return updatedProducts;
    }

    private List<ProductSpecCharAndValDTO> getUpdatedProductNew(
            List<ProductSpecCharAndValDTO> oldProducts, ProductSpecCharAndValDTO request) {
        List<ProductSpecCharAndValDTO> updatedProducts = new ArrayList<>();
        updatedProducts.add(request);
        Set<String> oldIds =
                oldProducts.stream().map(ProductSpecCharAndValDTO::getId).collect(Collectors.toSet());
        Set<String> newIds = updatedProducts.stream()
                .map(ProductSpecCharAndValDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        // validate xem id bản ghi update có match với id cũ không
        if (!oldIds.containsAll(newIds)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char.non.match");
        }
        return updatedProducts;
    }

    private List<ProductSpecCharValueDTO> getDeletedSpecCharValue(
            ProductSpecCharAndValDTO newProduct, ProductSpecCharAndValDTO oldProduct) {
        return oldProduct.getProductSpecCharValueDTOList().stream()
                .filter(p -> newProduct.getProductSpecCharValueDTOList().stream()
                        .map(ProductSpecCharValueDTO::getId)
                        .noneMatch(q -> Objects.equals(q, p.getId())))
                .collect(Collectors.toList());
    }

    private List<ProductSpecCharValueDTO> getUpdatedSpecCharValues(
            ProductSpecCharAndValDTO newProduct, ProductSpecCharAndValDTO oldProduct) {
        List<ProductSpecCharValueDTO> updatedValues = newProduct.getProductSpecCharValueDTOList().stream()
                .filter(p -> Objects.nonNull(p.getId()))
                .collect(Collectors.toList());
        Set<String> oldIds = oldProduct.getProductSpecCharValueDTOList().stream()
                .map(ProductSpecCharValueDTO::getId)
                .collect(Collectors.toSet());
        Set<String> newIds =
                updatedValues.stream().map(ProductSpecCharValueDTO::getId).collect(Collectors.toSet());
        if (!oldIds.containsAll(newIds)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, "product-spec-char-value.non.match");
        }
        return updatedValues;
    }

    private List<ProductSpecCharValueDTO> getUpdatedSpecCharValuesNew(
            ProductSpecCharAndValDTO newProduct, ProductSpecCharAndValDTO oldProduct) {
        List<ProductSpecCharValueDTO> updatedValues = newProduct.getProductSpecCharValueDTOList().stream()
                .filter(p -> Objects.nonNull(p.getId()))
                .collect(Collectors.toList());

        // Xác định các giá trị mới (có id là null)
        List<ProductSpecCharValueDTO> newValues = newProduct.getProductSpecCharValueDTOList().stream()
                .filter(p -> Objects.isNull(p.getId())) // Các giá trị có id là null (mới)
                .collect(Collectors.toList());

        updatedValues.addAll(newValues); // Thêm các giá trị mới vào danh sách cần cập nhật

        Set<String> oldIds = oldProduct.getProductSpecCharValueDTOList().stream()
                .map(ProductSpecCharValueDTO::getId)
                .collect(Collectors.toSet());

        Set<String> newIds =
                updatedValues.stream().map(ProductSpecCharValueDTO::getId).collect(Collectors.toSet());

        Set<String> deletedIds =
                oldIds.stream().filter(id -> !newIds.contains(id)).collect(Collectors.toSet());

        for (ProductSpecCharValueDTO deletedValue : oldProduct.getProductSpecCharValueDTOList()) {
            if (deletedIds.contains(deletedValue.getId())) {
                deletedValue.setStatus(0);
            }
        }

        updatedValues.addAll(oldProduct.getProductSpecCharValueDTOList().stream()
                .filter(value -> deletedIds.contains(value.getId()))
                .collect(Collectors.toList()));

        return updatedValues;
    }

    private List<ProductSpecCharValue> getNewSpecCharValues(ProductSpecCharAndValDTO newProduct) {
        List<ProductSpecCharValue> values = new ArrayList<>();
        List<ProductSpecCharValueDTO> newSpec;
        if (Objects.isNull(newProduct.getId())) {
            newProduct.setId(UUID.randomUUID().toString());
            newSpec = newProduct.getProductSpecCharValueDTOList();
        } else {
            newSpec = newProduct.getProductSpecCharValueDTOList().stream()
                    .filter(p -> Objects.isNull(p.getId()))
                    .collect(Collectors.toList());
        }
        if (!DataUtil.isNullOrEmpty(newSpec)) {
            for (ProductSpecCharValueDTO pro : newProduct.getProductSpecCharValueDTOList()) {
                ProductSpecCharValue value =
                        ObjectMapperFactory.getInstance().convertValue(pro, ProductSpecCharValue.class);
                value.setId(UUID.randomUUID().toString());
                value.setProductSpecCharId(newProduct.getId());
                value.setStatus(1);
                value.setState(1);
                value.setNew(true);
                values.add(value);
            }
        }
        return values;
    }

    private Mono<DataResponse> getValues(List<ProductSpecCharAndValDTO> products, Boolean isPreview) {
        return Flux.fromIterable(products)
                .flatMap(p -> productSpecCharValueRepository
                        .findByProductSpecCharId(p.getId(), isPreview)
                        .collectList()
                        .flatMap(vals -> {
                            p.addValues(vals);
                            return Mono.just(p);
                        })
                        .switchIfEmpty(Mono.just(p)))
                .collectList()
                .map(p -> new DataResponse(Translator.toLocaleVi(SUCCESS), p));
    }

    private Mono<Boolean> insertServiceCharacteristicList(List<ServiceCharacteristicDTO> serviceCharacteristicList) {
        List<ProductSpecChar> productSpecCharList = serviceCharacteristicList.stream()
                .map(serviceCharacteristic -> {
                    String uuid = UUID.randomUUID().toString();
                    ProductSpecChar productSpecChar = new ProductSpecChar();
                    productSpecChar.setId(uuid);
                    productSpecChar.setCode(serviceCharacteristic.getCode());
                    productSpecChar.setName(serviceCharacteristic.getName());
                    productSpecChar.setTelecomServiceId(serviceCharacteristic.getTelecomServiceId());
                    productSpecChar.setTelecomServiceAlias(serviceCharacteristic.getTelecomServiceAlias());
                    productSpecChar.setState(0);
                    productSpecChar.setStatus(1);
                    return productSpecChar;
                })
                .collect(Collectors.toList());
        var insertProductSpecCharListMono =
                productSpecCharRepository.saveAll(productSpecCharList).collectList();
        return AppUtils.insertData(insertProductSpecCharListMono).flatMap(insertRs -> {
            List<ProductSpecCharValue> productSpecCharValueList = new ArrayList<>();
            for (ProductSpecChar productSpec : productSpecCharList) {
                Optional<ServiceCharacteristicDTO> serviceCharacteristicOptional = serviceCharacteristicList.stream()
                        .filter(serviceCharacteristic -> DataUtil.safeEqual(
                                serviceCharacteristic.getTelecomServiceId(), productSpec.getTelecomServiceId()))
                        .findAny();
                if (serviceCharacteristicOptional.isEmpty()
                        || DataUtil.isNullOrEmpty(
                                serviceCharacteristicOptional.get().getProductSpecCharValueDTOList())) {
                    continue;
                }
                List<ProductSpecCharValue> tempProductSpecCharValueList =
                        serviceCharacteristicOptional.get().getProductSpecCharValueDTOList().stream()
                                .map(value -> {
                                    String uuid = UUID.randomUUID().toString();
                                    return ProductSpecCharValue.builder()
                                            .id(uuid)
                                            .productSpecCharId(productSpec.getId())
                                            .value(value.getValue())
                                            .name(value.getName())
                                            .status(1)
                                            .state(0)
                                            .build();
                                })
                                .collect(Collectors.toList());
                productSpecCharValueList.addAll(tempProductSpecCharValueList);
            }
            var insertProductSpecCharValueListMono = productSpecCharValueRepository
                    .saveAll(productSpecCharValueList)
                    .collectList();
            return AppUtils.insertData(insertProductSpecCharValueListMono);
        });
    }

    private Mono<Boolean> updateProductSpecCharList(List<ProductSpecCharAndValDTO> updateSpecCharValueList) {
        var updateSpecCharValueListMono = productSpecRepo
                .updateBatchProductSpecCharAndVal(updateSpecCharValueList)
                .collectList();
        return AppUtils.insertData(updateSpecCharValueListMono).flatMap(rs -> {
            List<ProductSpecCharValueDTO> productSpecCharValueDTOList = updateSpecCharValueList.stream()
                    .flatMap(spec -> spec.getProductSpecCharValueDTOList().stream())
                    .collect(Collectors.toList());
            var updateProductSpecCharValueListMono = productSpecRepo
                    .updateBatchProductSpecCharValue(productSpecCharValueDTOList)
                    .collectList();
            return AppUtils.insertData(updateProductSpecCharValueListMono);
        });
    }

    private Mono<Boolean> deleteProductSpecCharList(List<ProductSpecCharAndValDTO> productSpecCharList) {
        var deleteMono = productSpecRepo
                .deleteBatchProductSpecCharAndVal(productSpecCharList)
                .collectList();

        return AppUtils.insertData(deleteMono).flatMap(rs -> {
            List<ProductSpecCharValueDTO> productSpecCharValueDTOList = productSpecCharList.stream()
                    .flatMap(productSpecChar -> productSpecChar.getProductSpecCharValueDTOList().stream())
                    .collect(Collectors.toList());
            return deleteProductSpecCharValueList(productSpecCharValueDTOList);
        });
    }

    private Mono<Boolean> deleteProductSpecCharValueList(List<ProductSpecCharValueDTO> productSpecCharValueList) {
        var deleteMono = productSpecRepo
                .deleteBatchProductSpecCharValue(productSpecCharValueList)
                .collectList();
        return AppUtils.insertData(deleteMono);
    }

    private ProductSpecCharAndValDTO getAndRemoveProductSpec(
            String telecomServiceId, String code, List<ProductSpecCharAndValDTO> productSpecList) {
        Iterator iterator = productSpecList.iterator();
        while (iterator.hasNext()) {
            ProductSpecCharAndValDTO productSpec = (ProductSpecCharAndValDTO) iterator.next();
            if (DataUtil.safeEqual(productSpec.getTelecomServiceId(), telecomServiceId)
                    && DataUtil.safeEqual(productSpec.getCode(), code)) {
                iterator.remove();
                return productSpec;
            }
        }
        return null;
    }

    private Mono<LstServiceCharacteristicResponse> getServiceCharacteristic(String telecomServiceId) {
        return productClient.getLstProductSpec(telecomServiceId).flatMap(productSpecOptional -> {
            LstServiceCharacteristicResponse lstServiceCharacteristicResponse =
                    productSpecOptional.orElse(new LstServiceCharacteristicResponse());
            lstServiceCharacteristicResponse.setTelecomServiceId(telecomServiceId);
            return Mono.just(lstServiceCharacteristicResponse);
        });
    }

    private Mono<List<ProductSpecCharAndValDTO>> findAllActiveProductSpec() {
        return productSpecRepo.findAllActive().collectList().flatMap(specList -> {
            Map<String, ProductSpecCharAndValDTO> specMap = new LinkedHashMap<>();
            for (ProductSpecCharAndValDTO spec : specList) {
                if (specMap.containsKey(spec.getId())) {
                    specMap.get(spec.getId()).addValues(spec.getProductSpecCharValueDTOList());
                } else {
                    specMap.put(spec.getId(), spec);
                }
            }
            return Mono.just(new ArrayList<>(specMap.values()));
        });
    }

    @Override
    public Mono<DataResponse> getListSubscriberActive(FilterGetListSubscriberActive request) {
        return subscriberRepository
                .findByIdNoAndTelecomServiceIdIn(request.getIdNo(), request.getLstTelecomServiceId())
                .collectList()
                .map(list -> {
                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), list);
                });
    }

    @Override
    public Mono<DataResponse> getListSubscriberActiveByAlias(FilterGetListSubscriberActiveByAlias request) {
        return subscriberRepository
                .findByIdNoAndTelecomServiceAliasIn(request.getIdNo(), request.getLstTelecomServiceAlias())
                .collectList()
                .map(list -> {
                    return new DataResponse<>(Translator.toLocaleVi(SUCCESS), list);
                });
    }

    @Override
    public Mono<DataResponse> getListSubscriberActiveByListIdNoAndListAlias(GetListSubscriberActive request) {
        return subscriberRepository
                .findByIdNoInAndTelecomServiceAliasIn(request.getLstIdNo(), request.getLstTelecomServiceAlias())
                .collectList()
                .flatMap(list -> {
                    if (DataUtil.isNullOrEmpty(list)) {
                        return Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "subcriber.not.found"));
                    } else {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), list));
                    }
                });
    }

    @Override
    public Mono<DataResponse> syncSubscriberOrder(UpdateAccountServiceInfoDTO request) {
        // validate input
        validateRequestUpdateAcocuntServiceInfo(request);
        if (DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
            if (Constants.TelecomServiceId.CA.equals(request.getTelecomServiceId())) {
                request.setTelecomServiceAlias(AuthConstants.TelecomServiceAlias.MYSIGN);
            }
            if (Constants.TelecomServiceId.VCONTRACT.equals(request.getTelecomServiceId())) {
                request.setTelecomServiceAlias(AuthConstants.TelecomServiceAlias.VCONTRACT);
            }
        }
        if (DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
            return telecomRepository
                    .getByOriginId(String.valueOf(request.getTelecomServiceId()))
                    .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "telecomId.not.found")))
                    .flatMap(telecom -> {
                        request.setTelecomServiceAlias(telecom.getServiceAlias());
                        return syncSubscriberOrderAfterValidate(request);
                    });
        }
        return syncSubscriberOrderAfterValidate(request);
    }

    /**
     * Sync subscriber sau khi tien xu ly
     *
     * @param request
     * @return
     */
    private Mono<DataResponse> syncSubscriberOrderAfterValidate(UpdateAccountServiceInfoDTO request) {
        if (DataUtil.isNullOrEmpty(request.getIsdn())) {
            return cmClient.getListSubscriberByIdNo(request.getIdNo())
                    .flatMap(listSubscriber -> {
                        // filter by telecomServiceId
                        List<SubscriberCMResponse> subscriberCMResponses = listSubscriber.stream()
                                .filter(rs -> request.getTelecomServiceId().equals(rs.getTelecomServiceId()))
                                .collect(Collectors.toList());
                        return Flux.fromIterable(subscriberCMResponses)
                                .flatMap(subscriber -> {
                                    request.setIsdn(subscriber.getIsdn());
                                    return syncSubscriberOrderWhenIsDnNotNull(request);
                                })
                                .collectList();
                    })
                    .map(rs -> new DataResponse("success", request.getIdNo()));
        }

        return syncSubscriberOrderWhenIsDnNotNull(request);
    }

    /**
     * This is method for isDn not null
     *
     * @param request
     * @return
     */
    private Mono<DataResponse> syncSubscriberOrderWhenIsDnNotNull(UpdateAccountServiceInfoDTO request) {
        return renewalCAService
                .getSubscriberSmeInfo(
                        request.getTelecomServiceId(),
                        request.getIdNo(),
                        request.getIsdn(),
                        request.getTelecomServiceAlias()) // goi CM lay thong tin chi tiet thue bao
                .flatMap(data -> {
                    // check CM khong tra ve subscriber, tra ve > 1 ban ghi subscriber => bao loi
                    if (data.size() != 1) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_FOUND, "update.account.service.info.error.account.not.found"));
                    }
                    Subscriber subscriber = DataUtils.dtoToModel(data.getFirst());
                    subscriber.setTelecomServiceId(request.getTelecomServiceId());
                    subscriber.setTelecomServiceAlias(request.getTelecomServiceAlias());
                    return subscriberRepository
                            .findById(DataUtil.safeToString(subscriber.getId()))
                            .transform(DataUtils::optional) // check subscriber ton tai trong DB
                            .flatMap(subscriberDb -> {
                                // check subscriber ton tai trong DB
                                if (subscriberDb.isEmpty()) {
                                    // set id null de insert 1 id moi, tranh case trung lap id subscriber CM va DB
                                    return subscriberRepository.save(subscriber); // Neu khong tim thay trong DB =>
                                    // insert
                                } else {
                                    subscriber.setNew(false);
                                    return subscriberRepository.save(subscriber); // Neu tim thay subscriber trong DB =>
                                    // update
                                }
                            })
                            .flatMap(rs -> activeTelecomRepository
                                    .count(
                                            request.getTelecomServiceId(),
                                            request.getIdNo(),
                                            request.getTelecomServiceAlias()) // bo sung them alias
                                    .flatMap(count -> {
                                        ActiveTelecom activeTelecom = new ActiveTelecom();
                                        activeTelecom.setTelecomServiceId(request.getTelecomServiceId());
                                        activeTelecom.setAlias(request.getTelecomServiceAlias()); // bo sung alias cho
                                        // luong scontract
                                        activeTelecom.setIdNo(request.getIdNo());

                                        // if not exits => insert
                                        if (count == 0) {
                                            activeTelecom.setId(String.valueOf(UUID.randomUUID()));
                                            activeTelecom.setNew(true);
                                            return activeTelecomRepository.save(activeTelecom);
                                        }

                                        // return activeTelecom
                                        return Mono.just(activeTelecom);
                                    }))
                            .map(activeTelecom -> new DataResponse<>("success", activeTelecom.getIdNo()));
                });
    }

    /**
     * ham validate input cho syncSubscriberOrder
     *
     * @param request
     */
    private void validateRequestUpdateAcocuntServiceInfo(UpdateAccountServiceInfoDTO request) {
        if (DataUtil.isNullOrEmpty(request.getTelecomServiceId())) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "update.account.service.info.error.telecom.service.id.empty");
        }
        if (DataUtil.isNullOrEmpty(request.getIdNo())) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "update.account.service.info.error.id.no.empty");
        }
        // comment order chay vi Hub se trien khai truoc
        // if (DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
        // throw new BusinessException(CommonErrorCode.INVALID_PARAMS,
        // "update.account.service.info.error.telecom.service.alias.empty");
        // }
    }

    @Override
    public Mono<DataResponse<List<Subscriber>>> getTelecomServiceConnect(GetServiceConnectDTO request) {
        if (request == null
                || DataUtil.isNullOrEmpty(request.getOrganizationId())
                || (DataUtil.isNullOrEmpty(request.getServiceId())
                        && DataUtil.isNullOrEmpty(request.getTelecomServiceAlias()))) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }

        return authClient
                .getTrustedIdNoOrganization(request.getOrganizationId())
                .flatMap(lstIdNo -> {
                    if (DataUtil.isNullOrEmpty(lstIdNo)) {
                        return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "idNo.not.found"));
                    }
                    String idNo = lstIdNo.get(0);

                    if (!DataUtil.isNullOrEmpty(request.getServiceId())
                            && DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
                        return subscriberRepository
                                .findByIdNoAndTelecomServiceIdIn(
                                        idNo, Collections.singletonList(request.getServiceId()))
                                .collectList()
                                .map(list -> {
                                    if (DataUtil.isNullOrEmpty(list)) {
                                        return new DataResponse<>(
                                                "NOT_FOUND", Translator.toLocaleVi("service.not.connect"), null);
                                    } else {
                                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), list);
                                    }
                                });
                    } else if (DataUtil.isNullOrEmpty(request.getServiceId())
                            && !DataUtil.isNullOrEmpty(request.getTelecomServiceAlias())) {
                        return subscriberRepository
                                .findByIdNoAndTelecomServiceAliasIn(
                                        idNo, Collections.singletonList(request.getTelecomServiceAlias()))
                                .collectList()
                                .map(list -> {
                                    if (DataUtil.isNullOrEmpty(list)) {
                                        return new DataResponse<>(
                                                "NOT_FOUND", Translator.toLocaleVi("service.not.connect"), null);
                                    } else {
                                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), list);
                                    }
                                });
                    } else {
                        return subscriberRepository
                                .findByIdNoAndTelecomServiceIdAndAliasIn(
                                        idNo,
                                        Collections.singletonList(request.getServiceId()),
                                        Collections.singletonList(request.getTelecomServiceAlias()))
                                .collectList()
                                .map(list -> {
                                    if (DataUtil.isNullOrEmpty(list)) {
                                        return new DataResponse<>(
                                                "NOT_FOUND", Translator.toLocaleVi("service.not.connect"), null);
                                    } else {
                                        return new DataResponse<>(Translator.toLocaleVi(SUCCESS), list);
                                    }
                                });
                    }
                });
    }

    @Override
    public Mono<DataResponse<List<Telecom>>> getTelecomServiceRelated(
            String telecomServiceId, String telecomServiceAlias) {
        if (DataUtil.isNullOrEmpty(telecomServiceId) && DataUtil.isNullOrEmpty(telecomServiceAlias)) {
            return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "params.invalid"));
        }
        return telecomRepository
                .geRelatedServiceIdAndServiceAlias(telecomServiceId, telecomServiceAlias)
                .collectList()
                .flatMap(lstRelatedService -> {
                    return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), lstRelatedService));
                });
    }

    @Override
    public Mono<DataResponse> createFilterDetail(ProductSpecCharAndValDTO request) {
        return settingClient
                .getTelecomServiceByOriginId(request.getTelecomServiceId())
                .flatMap(telecoms -> {
                    if (DataUtil.isNullOrEmpty(telecoms)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.BAD_REQUEST, "telecom-service.not.found"));
                    }
                    TelecomDTO telecomDTO = telecoms.get(0);
                    return validateRequestDataNew(request).flatMap(result -> {
                        List<ProductSpecCharAndValDTO> newProductSpecChars =
                                getNewProductSpecCharNew(request, telecomDTO);
                        List<ProductSpecCharValue> newSpecCharValues = new ArrayList<>();
                        newSpecCharValues.addAll(getNewSpecCharValues(request));
                        return Mono.zip(
                                        createProductSpecChars(newProductSpecChars),
                                        productSpecCharValueRepository
                                                .saveAll(newSpecCharValues)
                                                .collectList())
                                .map(rs -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    });
                });
    }

    @Override
    public Mono<DataResponse> editFilter(ProductSpecCharAndValDTO request) {
        return settingClient
                .getTelecomServiceByOriginId(request.getTelecomServiceId())
                .flatMap(telecoms -> {
                    if (DataUtil.isNullOrEmpty(telecoms)) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.BAD_REQUEST, "telecom-service.not.found"));
                    }
                    return getFilterDetail(request.getTelecomServiceId(), null).flatMap(p -> {
                        // Kiem tra va xac thuc du lieu tu request
                        return validateRequestDataNew(request).flatMap(result -> {
                            if (DataUtil.isNullOrEmpty(request.getProductSpecCharValueDTOList())) {
                                return Mono.error(new BusinessException(
                                        CommonErrorCode.BAD_REQUEST, "the-filter-must-have-at-least-1-criterion"));
                            }
                            List<ProductSpecCharAndValDTO> oldProducts = (List<ProductSpecCharAndValDTO>) p.getData();
                            // Lay danh sach thong tin san pham cu
                            List<ProductSpecCharAndValDTO> updatedProductSpecChars =
                                    getUpdatedProductNew(oldProducts, request);
                            List<ProductSpecCharValueDTO> updatedSpecCharValues = new ArrayList<>();
                            ProductSpecCharAndValDTO oldProduct = null;
                            // neu co ID request , tim san pham cu tuong ung
                            if (Objects.nonNull(request.getId())) {
                                oldProduct = oldProducts.stream()
                                        .filter(pro -> Objects.equals(pro.getId(), request.getId()))
                                        .findFirst()
                                        .orElse(null);
                            }
                            // Neu tim thay san pham cu , lay thong tin gia tri thuoc tinh cu
                            if (oldProduct != null) {
                                updatedSpecCharValues.addAll(getUpdatedSpecCharValuesNew(request, oldProduct));
                            }
                            // tao danh sach can ca nhat va danh sach moi
                            List<ProductSpecCharValueDTO> updateList = new ArrayList<>(); // for existing IDs (update)
                            List<ProductSpecCharValue> newSpecCharValues = new ArrayList<>();
                            // Duyet qua danh sach gia tri thuoc tinh da cap nhat
                            for (ProductSpecCharValueDTO value : updatedSpecCharValues) {
                                if (Objects.nonNull(value.getId())) {
                                    // Neu co ID , them vao danh sach can cap nhat
                                    updateList.add(value);
                                } else {
                                    // Neu khong co ID , taoo moi ID va them vao danh sach moi
                                    value.setId(UUID.randomUUID().toString());
                                    value.setProductSpecCharId(request.getId());
                                    value.setState(1);
                                    ProductSpecCharValue newValue = convertFromSpecCharValueDTO(value);
                                    newSpecCharValues.add(newValue);
                                }
                            }
                            // Sử dụng Mono.zip để thực hiện các hoạt động cơ sở dữ liệu song song và trả về
                            // một Mono với kết quả
                            return Mono.zip(
                                            productSpecRepo
                                                    .updateProductSpecCharNew(updatedProductSpecChars)
                                                    .collectList()
                                                    .doOnError(throwable -> log.error(
                                                            "Save updateProductSpecCharNew error: {}",
                                                            throwable.getMessage())),
                                            productSpecRepo
                                                    .updateProductSpecCharValueNew(updateList)
                                                    .collectList()
                                                    .doOnError(throwable -> log.error(
                                                            "Save updateProductSpecCharValueNew error: {}",
                                                            throwable.getMessage())),
                                            productSpecCharValueRepository
                                                    .saveAll(newSpecCharValues)
                                                    .collectList()
                                                    .doOnError(throwable -> log.error(
                                                            "Save saveAll error: {}", throwable.getMessage())))
                                    .map(rs -> new DataResponse<>(Translator.toLocaleVi("success"), null));
                        });
                    });
                });
    }

    private ProductSpecCharValue convertFromSpecCharValueDTO(ProductSpecCharValueDTO valueDTO) {
        return ProductSpecCharValue.builder()
                .id(valueDTO.getId())
                .productSpecCharId(valueDTO.getProductSpecCharId())
                .name(valueDTO.getName())
                .value(valueDTO.getValue())
                .status(valueDTO.getStatus())
                .state(valueDTO.getState())
                .displayOrder((valueDTO).getDisplayOrder())
                .isNew(true)
                .build();
    }

    @Override
    public Mono<DataResponse> deleteFilter(String telecomServiceId, String productSpecCharId) {
        return settingClient.getTelecomServiceByOriginId(telecomServiceId).flatMap(telecoms -> {
            if (DataUtil.isNullOrEmpty(telecoms)) {
                return Mono.error(new BusinessException(CommonErrorCode.BAD_REQUEST, "telecom-service.not.found"));
            }
            // set status của bản ghi có id = productSpecCharId về 0
            return productSpecRepo
                    .deleteProductSpecChar(productSpecCharId)
                    .collectList()
                    .flatMap(rs -> {
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), null));
                    });
        });
    }
    ;
}
