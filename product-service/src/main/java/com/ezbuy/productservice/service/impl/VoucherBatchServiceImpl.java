package com.ezbuy.productservice.service.impl;

import com.ezbuy.productmodel.dto.request.CreateVoucherBatchRequest;
import com.ezbuy.productmodel.dto.request.VoucherBatchRequest;
import com.ezbuy.productmodel.dto.response.PaginationDTO;
import com.ezbuy.productmodel.dto.response.VoucherBatchSearchResponse;
import com.ezbuy.productmodel.model.VoucherBatch;
import com.ezbuy.productmodel.model.VoucherType;
import com.ezbuy.productservice.repository.VoucherBatchRepository;
import com.ezbuy.productservice.repository.VoucherTypeRepository;
import com.ezbuy.productservice.repository.repoTemplate.VoucherBatchRepositoryTemPlate;
import com.ezbuy.productservice.service.VoucherBatchService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
public class VoucherBatchServiceImpl extends BaseServiceHandler implements VoucherBatchService {
    private final VoucherBatchRepository voucherBatchRepository;
    private final VoucherTypeRepository voucherTypeRepository;
    private final VoucherBatchRepositoryTemPlate voucherBatchRepositoryTemPlate;
    private static final String INACTIVE = "INACTIVE";
    private static final String NEW = "NEW";

    @Override
    public Mono<List<VoucherBatch>> getAllVoucherBatch() {
        return voucherBatchRepository.getAllVoucherBatch().collectList();
    }

    @Override
    public Mono<List<VoucherType>> getAllVoucherType() {
        return voucherTypeRepository.getAllVoucherType().collectList();
    }

    @Override
    @Transactional
    public Mono<DataResponse<VoucherBatch>> createVoucherBatch(CreateVoucherBatchRequest request) {
        var getSysDate = voucherBatchRepository.getSysDate();
        return Mono.zip(SecurityUtils.getCurrentUser(), validateRequestVoucher(request, true), getSysDate)
                .flatMap(userValidate -> {
                    LocalDateTime now = userValidate.getT3();
                    VoucherBatch voucherBatch = VoucherBatch.builder()
                            .id(UUID.randomUUID().toString())
                            .code(request.getCode())
                            .description(request.getDescription())
                            .voucherTypeId(request.getVoucherTypeId())
                            .quantity(request.getQuantity())
                            .expiredDate(request.getExpiredDate())
                            .expiredPeriod(request.getExpiredPeriod())
                            .state(request.getState())
                            .createAt(now)
                            .createBy(userValidate.getT1().getUsername())
                            .isNew(true)
                            .build();
                    return voucherBatchRepository
                            .save(voucherBatch)
                            .onErrorReturn(new VoucherBatch())
                            .flatMap(result -> Mono.just(DataResponse.success(result)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> updateVoucherBatch(String id, CreateVoucherBatchRequest request) {
        var getSysDate = voucherBatchRepository.getSysDate();
        return Mono.zip(validateRequestVoucher(request, false), SecurityUtils.getCurrentUser(), getSysDate)
                .flatMap(validateUser -> voucherBatchRepository
                        .findFirstById(request.getId())
                        .flatMap(voucherDb -> {
                            LocalDateTime now = validateUser.getT3();
                            voucherDb.setCode(request.getCode());
                            voucherDb.setDescription(request.getDescription());
                            voucherDb.setVoucherTypeId(request.getVoucherTypeId());
                            voucherDb.setQuantity(request.getQuantity());
                            voucherDb.setExpiredDate(request.getExpiredDate());
                            voucherDb.setExpiredPeriod(request.getExpiredPeriod());
                            voucherDb.setState(request.getState());
                            voucherDb.setUpdateAt(now);
                            voucherDb.setUpdateBy(validateUser.getT2().getUsername());
                            voucherDb.setNew(false);
                            return voucherBatchRepository
                                    .save(voucherDb)
                                    .map(rsp -> DataResponse.success(true))
                                    .onErrorResume(throwable -> Mono.error(new BusinessException(
                                            CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")));
                        }));
    }

    @Override
    @Transactional
    public Mono<DataResponse<VoucherBatch>> getVoucherBatch(String id) {
        return voucherBatchRepository
                .findFirstById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "voucher.batch.found")))
                .map(voucherBatch -> new DataResponse<>(Translator.toLocale("Success"), voucherBatch));
    }

    private Mono<Boolean> validateRequestVoucher(CreateVoucherBatchRequest request, boolean isCreate) {
        if (DataUtil.isNullOrEmpty(request.getCode())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.code.empty"));
        }
        if (DataUtil.isNullOrEmpty(request.getVoucherTypeId())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.type.empty"));
        }
        if (DataUtil.isNullOrEmpty(request.getQuantity())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.quantity.empty"));
        }
        if (DataUtil.isNullOrEmpty(request.getState())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.state.empty"));
        }
        if (request.getCode().length() > 200) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.error.code.length"));
        }
        if (!DataUtil.isNullOrEmpty(request.getDescription())) {
            if (request.getDescription().length() > 200) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.description.length"));
            }
        }
        if (request.getVoucherTypeId().length() > 36) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucherTypeId.length"));
        }
        if (request.getState().length() > 20) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.state.length"));
        }
        if (request.getQuantity() < 0) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.quantity"));
        }
        // check exist code in db
        return voucherBatchRepository
                .getVBByCodeAndVouType(request.getCode(), request.getVoucherTypeId())
                .flatMap(voucher -> {
                    if (isCreate) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.code.exist"));
                    } else {
                        assert voucher.getId() != null;
                        if (!voucher.getId().equals(request.getId())) {
                            return Mono.error(
                                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.code.exist"));
                        }
                        // neu state = new thi cho phep chuyen state = inactive
                        if (INACTIVE.equals(request.getState())) {
                            if (!(NEW.equals(voucher.getState()) || INACTIVE.equals(voucher.getState()))) {
                                return Mono.error(new BusinessException(
                                        CommonErrorCode.INVALID_PARAMS, "voucher.batch.state.inactive"));
                            }
                        }
                    }
                    return Mono.just(true);
                })
                .switchIfEmpty(Mono.just(true));
    }

    @Override
    public Mono<VoucherBatchSearchResponse> searchVoucherBatch(VoucherBatchRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.date.request.invalid"));
        }
        if (!Objects.isNull(request.getFromDate())
                && request.getFromDate().isAfter(request.getToDate().plusDays(1))) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.from-date.larger.to-date"));
        }
        if ((Objects.isNull(request.getFromExpiredDate()) && Objects.nonNull(request.getToExpiredDate()))
                || (Objects.nonNull(request.getFromExpiredDate()) && Objects.isNull(request.getToExpiredDate()))) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.date.request.invalid"));
        }
        if (!Objects.isNull(request.getFromExpiredDate())
                && request.getFromExpiredDate()
                        .isAfter(request.getToExpiredDate().plusDays(1))) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.from-date.larger.to-date"));
        }
        Flux<VoucherBatch> pages = voucherBatchRepositoryTemPlate.queryVoucherBatch(request);
        Mono<Long> countMono = voucherBatchRepositoryTemPlate.countVoucherBatch(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());
            VoucherBatchSearchResponse response = new VoucherBatchSearchResponse();
            response.setVoucherBatch(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }
}
