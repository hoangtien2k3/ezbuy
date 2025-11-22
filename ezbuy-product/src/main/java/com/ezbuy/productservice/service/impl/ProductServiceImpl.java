package com.ezbuy.productservice.service.impl;

import static com.ezbuy.productservice.constants.Constants.LOCK_STATUS.LOCK_STATUS_LOCK;
import static com.ezbuy.productservice.constants.Constants.LOCK_STATUS.LOCK_STATUS_UNLOCK;
import static com.ezbuy.productservice.constants.Constants.Message.SUCCESS;
import static com.ezbuy.productservice.constants.Constants.REVENUE_RATIO_LIST;
import static com.ezbuy.productservice.constants.Constants.TAX_RATIO_LIST;

import com.ezbuy.productservice.model.dto.OrganizationUnit;
import com.ezbuy.productservice.model.dto.request.GetProductInfoRequest;
import com.ezbuy.productservice.model.dto.request.LockMultiProductRequest;
import com.ezbuy.productservice.model.dto.request.LockProductRequest;
import com.ezbuy.productservice.model.dto.request.SearchProductRequest;
import com.ezbuy.productservice.model.dto.response.GetProductInfoResponse;
import com.ezbuy.productservice.model.dto.response.ProductSearchResult;
import com.ezbuy.productservice.model.entity.Product;
import com.ezbuy.productservice.repository.ProductRepository;
import com.ezbuy.productservice.repository.repoTemplate.ProductCustomRepository;
import com.ezbuy.productservice.service.ProductService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.Translator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductCustomRepository productCustomRepository;
    private final ProductRepository productRepository;

    private static final String SYNC_TYPE = "PRODUCT";
    private static final String NOT_FOUND = "product.input.notFound";
    private static final String COMMON_ERROR = "common.error";
    private static final Integer STATUS_ACTIVE = 1;

    private Mono<Boolean> validateRequestCreateProduct(Product product, boolean isCreate) {
        product.trim();
        if (DataUtil.isNullOrEmpty(product.getCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.empty"));
        }
        if (DataUtil.isNullOrEmpty(product.getName())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.name.empty"));
        }
        if (DataUtil.isNullOrEmpty(product.getTaxRatio())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.tax.ratio.empty"));
        }
        if (product.getCode().length() > 15) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.length"));
        }
        if (product.getName().length() > 500) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.name.length"));
        }
        if (!DataUtil.isNullOrEmpty(product.getUnit()) && product.getUnit().length() > 150) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.unit.length"));
        }
        if (product.getPriceImport() != null
                && (product.getPriceImport() < 0D || product.getPriceImport() >= 10000000000D)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.price.import.invalid"));
        }
        if (product.getPriceExport() != null
                && (product.getPriceExport() < 0D || product.getPriceExport() >= 10000000000D)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.price.export.invalid"));
        }
        if (product.getDiscount() != null && (product.getDiscount() < 0D || product.getDiscount() >= 10000000000D)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.discount.invalid"));
        }
        if (!TAX_RATIO_LIST.contains(product.getTaxRatio())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.tax.ratio.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getRevenueRatio())
                && !REVENUE_RATIO_LIST.contains(DataUtil.safeToString(product.getRevenueRatio()))) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.revenue.ratio.invalid"));
        }
        if (!DataUtil.isNullOrEmpty(product.getPriceExport())
                && !DataUtil.isNullOrEmpty(product.getDiscount())
                && product.getPriceExport() < product.getDiscount()) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.price.export.smaller.than.discount"));
        }
        return productRepository
                .findFirstByCode(product.getCode())
                .switchIfEmpty(Mono.just(new Product()))
                .flatMap(productDb -> {
                    if (isCreate && !DataUtil.isNullOrEmpty(productDb.getCode())) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.exist"));
                    }
                    if (!isCreate && !DataUtil.isNullOrEmpty(productDb.getCode()) && !DataUtil.safeEqual(product.getId(), productDb.getId())) {
                        return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "product.error.code.exist"));
                    }
                    return Mono.just(true);
                });
    }

    @Override
    public Mono<ProductSearchResult> searchProduct(SearchProductRequest request, String organizationId) {
        if (request.getPageIndex() == null) {
            request.setPageIndex(1);
        }
        if (request.getPageSize() == null) {
            request.setPageSize(10);
        }
        return Mono.zip(productCustomRepository.countProduct(request, organizationId),
                        productCustomRepository.searchProduct(request, organizationId).collectList())
                .flatMap(searchResult -> Mono.just(ProductSearchResult.builder()
                        .total(searchResult.getT1())
                        .pageIndex(request.getPageIndex())
                        .pageSize(request.getPageSize())
                        .dataList(searchResult.getT2())
                        .build()));
    }

    @Override
    public Mono<DataResponse<Product>> handleCreateProduct(
            Product product, List<OrganizationUnit> unitList, String organizationId) {
        return Mono.zip(SecurityUtils.getCurrentUser(), validateRequestCreateProduct(product, true))
                .flatMap(userValidate -> {
                    String username = userValidate.getT1().getUsername();
                    String unitName = !DataUtil.isNullOrEmpty(unitList) ? unitList.getFirst().getName() : StringUtils.EMPTY;
                    product.setId(UUID.randomUUID().toString());
                    product.setNew(true);
                    product.setLockStatus(LOCK_STATUS_UNLOCK);
                    product.setStatus(STATUS_ACTIVE);
                    product.setCreateUnit(unitName);
                    product.setCreateAt(LocalDateTime.now());
                    product.setUpdateAt(LocalDateTime.now());
                    product.setCreateBy(username);
                    product.setUpdateBy(username);
                    product.setOrganizationId(organizationId);
                    return productRepository
                            .save(product)
                            .map(rsp -> new DataResponse<>(SUCCESS, rsp))
                            .onErrorReturn(new DataResponse<>(CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi(COMMON_ERROR), new Product()))
                            .doOnError(throwable -> log.error("Save product error: {}", throwable.getMessage()));
                });
    }

    @Override
    public Mono<Product> detailProduct(String productId) {
        return productRepository
                .findFirstById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist")))
                .map(product -> product);
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> updateProduct(Product product) {
        return Mono.zip(validateRequestCreateProduct(product, false), SecurityUtils.getCurrentUser())
                .flatMap(validateUser -> productRepository
                        .findFirstById(product.getId())
                        .flatMap(productDb -> {
                            productDb.setCode(product.getCode());
                            productDb.setName(product.getName());
                            productDb.setPriceImport(product.getPriceImport());
                            productDb.setPriceExport(product.getPriceExport());
                            productDb.setUnit(product.getUnit());
                            productDb.setDiscount(product.getDiscount());
                            productDb.setTaxRatio(product.getTaxRatio());
                            productDb.setRevenueRatio(product.getRevenueRatio());
                            productDb.setUpdateAt(LocalDateTime.now());
                            productDb.setUpdateBy(validateUser.getT2().getUsername());
                            productDb.setNew(false);
                            productDb.setStatus(STATUS_ACTIVE);
                            return productRepository
                                    .save(productDb)
                                    .map(rsp -> DataResponse.success(true))
                                    .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                        }));
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> deleteProduct(String productId) {
        return Mono.zip(productRepository.findFirstByIdAndStatus(productId, STATUS_ACTIVE)
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist.or.inactive"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productUser -> {
                    Product product = productUser.getT1();
                    product.setNew(false);
                    product.setStatus(STATUS_ACTIVE);
                    product.setUpdateAt(LocalDateTime.now());
                    product.setUpdateBy(productUser.getT2().getUsername());
                    return productRepository
                            .save(product)
                            .map(rsp -> DataResponse.success(true))
                            .onErrorResume(error -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> lockProduct(LockProductRequest request) {
        return Mono.zip(
                        productRepository
                                .findFirstById(request.getProductId())
                                .switchIfEmpty(Mono.error(
                                        new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productUser -> {
                    Product product = productUser.getT1();
                    product.setNew(false);
                    product.setLockStatus(
                            DataUtil.safeEqual(product.getLockStatus(), LOCK_STATUS_LOCK)
                                    ? LOCK_STATUS_UNLOCK
                                    : LOCK_STATUS_LOCK);
                    product.setUpdateAt(LocalDateTime.now());
                    product.setUpdateBy(productUser.getT2().getUsername());
                    return productRepository
                            .save(product)
                            .map(rsp -> DataResponse.success(true))
                            .onErrorResume(error -> Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    public Mono<Boolean> lockMultiProduct(LockMultiProductRequest request) {
        return Mono.zip(productRepository.findAllByIdIn(request.getProductIdList())
                                .collectList()
                                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "product.error.not.exist"))),
                        SecurityUtils.getCurrentUser())
                .flatMap(productListUser -> {
                    List<Product> productDbList = productListUser.getT1();
                    productDbList.forEach(product -> {
                        product.setNew(false);
                        product.setLockStatus(DataUtil.safeEqual(product.getLockStatus(), LOCK_STATUS_LOCK) ? LOCK_STATUS_UNLOCK : LOCK_STATUS_LOCK);
                        product.setUpdateAt(LocalDateTime.now());
                        product.setUpdateBy(productListUser.getT2().getUsername());
                    });
                    return productRepository
                            .saveAll(productDbList)
                            .collectList()
                            .map(rsp -> true)
                            .onErrorResume(error -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, COMMON_ERROR)));
                });
    }

    @Override
    public Mono<GetProductInfoResponse> getProductInfo(GetProductInfoRequest request) {
        String type = DataUtil.safeTrim(request.getType());
        if (DataUtil.isNullOrEmpty(type)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.type.not.empty"));
        }
        UUID organizationId = request.getOrganizationId();
        List<String> ids = request.getIds();
        if (DataUtil.isNullOrEmpty(ids) && DataUtil.isNullOrEmpty(organizationId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.ids.not.empty"));
        }
        if (!SYNC_TYPE.equals(type)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "sync.history.type.invalid"));
        }
        Integer offset = request.getOffset();
        Integer limit = request.getLimit();
        if (offset != null && offset < 0) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("sync.history.params.pageIndex.invalid")));
        }
        if (limit != null && limit < 1) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("sync.history.params.pageSize.invalid")));
        }
        return productCustomRepository
                .getProductByIdAndOrganizationIdAndTransId(ids, organizationId, offset, limit, request.getTransactionId())
                .collectList()
                .switchIfEmpty(Mono.error(new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()), Translator.toLocaleVi(NOT_FOUND, "Hàng hóa"))))
                .map(productList -> {
                    List<String> lstCustId = productList.stream().map(Product::getId).collect(Collectors.toList());
                    GetProductInfoResponse getProductInfoResponse = new GetProductInfoResponse();
                    getProductInfoResponse.setRecords(productList);
                    getProductInfoResponse.setIds(lstCustId);
                    return getProductInfoResponse;
                });
    }
}
