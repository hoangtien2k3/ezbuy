package com.ezbuy.productservice.service.impl;

import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.dto.request.GenVoucherRequest;
import com.ezbuy.productmodel.dto.request.SearchVoucherRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.dto.request.VoucherRequest;
import com.ezbuy.productmodel.dto.response.PaginationDTO;
import com.ezbuy.productmodel.dto.response.VoucherSearchResponse;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productmodel.model.VoucherBatch;
import com.ezbuy.productmodel.model.VoucherUse;
import com.ezbuy.productservice.repository.VoucherBatchRepository;
import com.ezbuy.productservice.repository.VoucherRepository;
import com.ezbuy.productservice.repository.VoucherTypeRepository;
import com.ezbuy.productservice.repository.VoucherUseRepository;
import com.ezbuy.productservice.repository.repoTemplate.VoucherRepositoryTemplate;
import com.ezbuy.productservice.service.VoucherService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

import static com.ezbuy.ordermodel.constants.Constants.SYSTEM;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_STATE.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VoucherServiceImpl extends BaseServiceHandler implements VoucherService {
    private final VoucherBatchRepository voucherBatchRepository;
    private final VoucherRepositoryTemplate voucherRepositoryTemplate;
    private final VoucherRepository voucherRepository;
    private final VoucherUseRepository voucherUseRepository;
    private final VoucherTypeRepository voucherTypeRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    @Override
    public Mono<VoucherSearchResponse> searchVoucher(SearchVoucherRequest request) {
        int pageIndex = validatePageIndex(request.getPageIndex());
        request.setPageIndex(pageIndex);
        int pageSize = validatePageSize(request.getPageSize(), 10);
        request.setPageSize(pageSize);
        if ((Objects.isNull(request.getFromDate()) && Objects.nonNull(request.getToDate()))
                || (Objects.nonNull(request.getFromDate()) && Objects.isNull(request.getToDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.date.request.invalid"));
        }
        if (!Objects.isNull(request.getFromDate()) && request.getFromDate()
                .isAfter(ChronoLocalDate.from(request.getToDate().plusDays(1)))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.from-date.larger.to-date"));
        }
        if ((Objects.isNull(request.getFromExpiredDate()) && Objects.nonNull(request.getToExpiredDate()))
                || (Objects.nonNull(request.getFromExpiredDate()) && Objects.isNull(request.getToExpiredDate()))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.date.request.invalid"));
        }
        if (!Objects.isNull(request.getFromExpiredDate()) && request.getFromExpiredDate()
                .isAfter(request.getToExpiredDate().plusDays(1))) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocaleVi("params.from-date.larger.to-date"));
        }
        Flux<Voucher> pages = voucherRepositoryTemplate.queryVoucher(request);
        Mono<Long> countMono = voucherRepositoryTemplate.countVoucher(request);
        return Mono.zip(pages.collectList(), countMono).map(zip -> {
            PaginationDTO pagination = new PaginationDTO();
            pagination.setPageIndex(request.getPageIndex());
            pagination.setPageSize(request.getPageSize());
            pagination.setTotalRecords(zip.getT2());
            VoucherSearchResponse response = new VoucherSearchResponse();
            response.setVoucher(zip.getT1());
            response.setPagination(pagination);
            return response;
        });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Voucher>> createVoucher(VoucherRequest request) {
        var getSysDate = voucherBatchRepository.getSysDate();
        return Mono.zip(
                SecurityUtils.getCurrentUser(),
                validateRequestVoucher(request, true),
                getSysDate
        ).flatMap(userValidate -> {
                    LocalDateTime now = userValidate.getT3();
                    Voucher voucher = Voucher.builder()
                            .id(UUID.randomUUID().toString())
                            .code(request.getCode())
                            .voucherTypeId(request.getVoucherTypeId())
                            .batchId(request.getBatchId())
                            .expiredDate(request.getExpiredDate())
                            .expiredPeriod(request.getExpiredPeriod())
                            .state(request.getState())
                            .createAt(now)
                            .createBy(userValidate.getT1().getUsername())
                            .isNew(true)
                            .build();
                    return voucherRepository.save(voucher)
                            .onErrorReturn(new Voucher())
                            .flatMap(result -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), result)));
                }
        );
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> updateVoucher(String id, VoucherRequest request) {
        var getSysDate = voucherBatchRepository.getSysDate();
        return Mono.zip(
                        validateRequestVoucher(request, false),
                        SecurityUtils.getCurrentUser(),
                        getSysDate)
                .flatMap(validateUser -> voucherRepository.findFirstById(request.getId())
                        .flatMap(voucherDb -> {
                            LocalDateTime now = validateUser.getT3();
                            voucherDb.setCode(request.getCode());
                            voucherDb.setBatchId(request.getBatchId());
                            voucherDb.setVoucherTypeId(request.getVoucherTypeId());
                            voucherDb.setExpiredDate(request.getExpiredDate());
                            voucherDb.setExpiredPeriod(request.getExpiredPeriod());
                            voucherDb.setState(request.getState());
                            voucherDb.setUpdateAt(now);
                            voucherDb.setUpdateBy(validateUser.getT2().getUsername());
                            voucherDb.setNew(false);
                            return voucherRepository.save(voucherDb).map(rsp -> DataResponse.success(true))
                                    .onErrorResume(throwable -> Mono.error(
                                            new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                    "common.error")));
                        })
                );
    }

    private Mono<Boolean> validateRequestVoucher(VoucherRequest request, boolean isCreate) {
        if (DataUtil.isNullOrEmpty(request.getCode())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.code.empty"));
        }
        if (DataUtil.isNullOrEmpty(request.getVoucherTypeId())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.type.empty"));
        }

        if (DataUtil.isNullOrEmpty(request.getState())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.batch.state.empty"));
        }
        if (request.getCode().length() > 200) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
                    "voucher.error.code.length"));
        }
        if (request.getVoucherTypeId().length() > 36) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucherTypeId.length"));
        }
        if (request.getState().length() > 20) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.state.length"));
        }
        //check exist code in db
        return voucherRepository.getVoucher(request.getCode(), request.getBatchId(), request.getVoucherTypeId())
                .flatMap(voucher -> {
                    //neu isCreate = true thi bao trung ma voucher con = false (tuc la cap nhat) thi cho di tiep
                    if (isCreate) {
                        return Mono.error(
                                new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.code.exist"));
                    } else {
                        assert voucher.getId() != null;
                        if (!voucher.getId().equals(request.getId())) {
                            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
                                    "voucher.code.exist"));
                        }
                    }
                    return Mono.just(true);
                }).switchIfEmpty(Mono.just(true));
    }

    @Override
    @Transactional
    public Mono<DataResponse<Voucher>> updateStateVoucher(String voucherId, String voucherTypeId, String state) {
        String voucherTypeIdTrim = DataUtil.safeTrim(voucherTypeId);
        String voucherIdTrim = DataUtil.safeTrim(voucherId);
        if (DataUtil.isNullOrEmpty(voucherTypeIdTrim) && DataUtil.isNullOrEmpty(voucherIdTrim)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.type.id.or.voucher.id.empty"));
        }
        String stateTrim = DataUtil.safeTrim(state);
        if (DataUtil.isNullOrEmpty(stateTrim)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.empty"));
        }
        if (!NEW.equals(stateTrim)
                && !INACTIVE.equals(stateTrim)
                && !LOCKED.equals(stateTrim)
                && !Constants.VOUCHER_STATE.USED.equals(stateTrim)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.is.valid"));
        }
        Mono<Voucher> getInfoVoucher;
        if (DataUtil.isNullOrEmpty(voucherIdTrim)) {
            getInfoVoucher = voucherRepository.findVoucherByVoucherTypeId(voucherTypeIdTrim);
        } else {
            getInfoVoucher = voucherRepository.findVoucherByVoucherId(voucherIdTrim);
        }
        var getSysDate = voucherRepository.getSysDate();
        return Mono.zip(getInfoVoucher.switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, Translator.toLocale("voucher.by.voucher.type.id.empty")))),
                        getSysDate)
                .flatMap(tuple -> {
                    LocalDateTime now = tuple.getT2();
                    Voucher voucher = tuple.getT1();
                    String prevState = DataUtil.safeEqual(stateTrim, LOCKED) ? NEW : LOCKED;
                    var updateVoucher = voucherRepository.updateVoucherStateByPreviousState(stateTrim, now, SYSTEM, prevState, voucher.getId());
                    return updateVoucher.flatMap(update -> {
                        if (DataUtil.safeEqual(update, 0L)) {
                            return Mono.error(new BusinessException(CommonErrorCode.SQL_ERROR, "common.error"));
                        }
                        return Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), voucher));
                    }).onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")));
                });
    }

    @Override
    public Mono<DataResponse<Voucher>> findVoucherNewByTypeCode(String code) {
        code = DataUtil.safeTrim(code);
        if (DataUtil.isNullOrEmpty(code)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucher.code.empty"));
        }
        return voucherRepository.findVoucherNewByVoucherTypeCode(code)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.error.voucher.not.found")))
                .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocale("Success"), response)));
    }

    @Override
    public Mono<DataResponse<Voucher>> findVoucherByCode(String code) {
        code = DataUtil.safeTrim(code);
        if (DataUtil.isNullOrEmpty(code)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucher.code.empty"));
        }
        return voucherRepository.findVoucherByVoucherCode(code)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.error.voucher.not.found")))
                .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocale("Success"), response)));
    }

    @Override
    @Transactional
    public Mono<DataResponse<String>> unlockVoucher(UnlockVoucherRequest unlockVoucherRequest) {
        // lay danh sach voucher va voucher use het han
        return Mono.zip(
                        voucherUseRepository.getAllExpiredVoucherUse(unlockVoucherRequest.getExpiredMinutes()).collectList(),
                        voucherRepository.getAllExpiredVoucher(unlockVoucherRequest.getExpiredMinutes()).collectList(),
                        voucherRepository.getSysDate())
                .flatMap(voucherUseListSysDate -> {
                    List<VoucherUse> voucherUseList = voucherUseListSysDate.getT1();
                    List<Voucher> voucherList = voucherUseListSysDate.getT2();
                    LocalDateTime now = voucherUseListSysDate.getT3();
                    // update inactive voucher
                    Flux<VoucherUse> updateVoucherUse;
                    if (!DataUtil.isNullOrEmpty(voucherUseList)) {
                        voucherUseList.forEach(voucherUse -> {
                            voucherUse.setNew(false);
                            voucherUse.setState(INACTIVE);
                            voucherUse.setUpdateBy(SYSTEM);
                            voucherUse.setUpdateAt(now);
                        });
                        updateVoucherUse = voucherUseRepository.saveAll(voucherUseList);
                    } else {
                        updateVoucherUse = Flux.fromIterable(new ArrayList<>());
                    }
                    // update mo lai voucher
                    Flux<Voucher> updateVoucher;
                    if (!DataUtil.isNullOrEmpty(voucherList)) {
                        voucherList.forEach(voucher -> {
                            voucher.setNew(false);
                            voucher.setState(NEW);
                            voucher.setUpdateBy(SYSTEM);
                            voucher.setUpdateAt(now);
                        });
                        updateVoucher = voucherRepository.saveAll(voucherList);
                    } else {
                        updateVoucher = Flux.fromIterable(new ArrayList<>());
                    }
                    return Mono.zip(
                                    updateVoucherUse.collectList(),
                                    updateVoucher.collectList()
                            ).flatMap(update -> Mono.just(DataResponse.success(Translator.toLocale("unlock.voucher.completed"))))
                            .onErrorResume(throwable -> Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<String>> genVoucher(GenVoucherRequest unlockVoucherRequest) {
        // validate request
        String voucherBatchId = DataUtil.safeTrim(unlockVoucherRequest.getVoucherBatchId());
        if (DataUtil.isNullOrEmpty(voucherBatchId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.error.voucher.batch.id.empty")));
        }
        String voucherTypeId = DataUtil.safeTrim(unlockVoucherRequest.getVoucherTypeId());
        if (DataUtil.isNullOrEmpty(voucherTypeId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.error.voucher.type.id.empty")));
        }
        // lay thong tin voucher batch va check voucher chua su dung
        return Mono.zip(
                voucherRepository.findVoucherUnused(voucherTypeId).collectList(),
                voucherBatchRepository.findFirstById(voucherBatchId)
                        .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.batch.found")))),
                voucherTypeRepository.getById(voucherTypeId)
                        .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS,
                                Translator.toLocale("voucher-type.error.not.exist"))))
        ).flatMap(voucherListBatchType -> {
            // validate neu van con voucher chua su dung => bao loi
            if (!DataUtil.isNullOrEmpty(voucherListBatchType.getT1())) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.error.please.cancel.voucher.unused")));
            }
            // validate trang thai voucher batch khong hop le
            VoucherBatch voucherBatch = voucherListBatchType.getT2();
            if (DataUtil.isNullOrEmpty(voucherBatch.getState()) || !DataUtil.safeEqual(voucherBatch.getState().toLowerCase(), NEW)) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.batch.state.not.new")));
            }
            // validate so luong voucher khong hop le
            if (DataUtil.isNullOrEmpty(voucherBatch.getQuantity()) || voucherBatch.getQuantity() > 100000) {
                return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.batch.quantity.limit")));
            }
            return updateVoucherBatchState(voucherBatchId, Constants.VOUCHER_BATCH_STATE.INPROGRESS)
                    .thenReturn(DataResponse.success(Translator.toLocale("completed-to-insert-voucher")))
                    .onErrorResume(throwable -> updateVoucherBatchState(voucherBatchId, Constants.VOUCHER_BATCH_STATE.FAILED)
                            .thenReturn(DataResponse.failed(Translator.toLocale("failed-to-insert-voucher")))
                    );
        });
    }

    private Mono<Void> batchInsertVouchers(List<Voucher> voucherList) {
        final int BATCH_SIZE = 1000;
        List<Mono<Void>> batchInsertMonos = new ArrayList<>();
        for (int i = 0; i < voucherList.size(); i += BATCH_SIZE) {
            List<Voucher> batch = voucherList.subList(i, Math.min(i + BATCH_SIZE, voucherList.size()));
            batchInsertMonos.add(voucherRepository.saveAll(batch).then());
        }
        return Flux.concat(batchInsertMonos).then();
    }

    private Mono<Void> updateVoucherBatchState(String voucherBatchId, String newState) {
        return voucherBatchRepository.findById(voucherBatchId)
                .flatMap(voucherBatch -> {
                    voucherBatch.setState(newState);
                    return voucherBatchRepository.save(voucherBatch);
                }).then();
    }

    private List<String> generateVoucherCode(String voucherTypeCode, String voucherBatchCode,
                                             Integer quantity) {
        int numberOfCodes = quantity;
        int codeLength = 6;
        Set<String> uniqueCodes = generateUniqueCodes(voucherTypeCode, voucherBatchCode, numberOfCodes, codeLength);
        return new ArrayList<>(uniqueCodes);
    }

    private Set<String> generateUniqueCodes(String voucherTypeCode, String voucherBatchCode, int numberOfCodes, int codeLength) {
        Set<String> uniqueCodes = new HashSet<>();

        while (uniqueCodes.size() < numberOfCodes) {
            String code = generateRandomCode(voucherTypeCode, voucherBatchCode, codeLength);
            uniqueCodes.add(code);
        }

        return uniqueCodes;
    }

    private String generateRandomCode(String voucherTypeCode, String voucherBatchCode, Integer length) {
        StringBuilder randomCode = new StringBuilder();
        randomCode.append(voucherTypeCode);
        randomCode.append("w");
        randomCode.append(voucherBatchCode);
        randomCode.append("w");
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            randomCode.append(CHARACTERS.charAt(index));
        }
        return randomCode.toString();
    }

    @Override
    @Transactional
    public Mono<DataResponse<String>> insertVoucher() {
        return voucherBatchRepository.findVoucherBatchInprogress().collectList().flatMap(result -> {
            if (!DataUtil.isNullOrEmpty(result)) {
                var getSysDate = voucherRepository.getSysDate();
                return Mono.zip(
                                SecurityUtils.getCurrentUser(),
                                getSysDate,
                                voucherTypeRepository.getById(result.getFirst().getVoucherTypeId()))
                        .flatMap(result1 -> {
                            if (!DataUtil.isNullOrEmpty(result1.getT3())) {
                                List<String> voucherCodeList = generateVoucherCode(result1.getT3().getCode(),
                                        result.getFirst().getCode(), result.getFirst().getQuantity());
                                List<Voucher> voucherList = new ArrayList<>();
                                LocalDateTime now = result1.getT2();
                                for (String voucherCode : voucherCodeList) {
                                    Voucher voucher = Voucher.builder()
                                            .id(UUID.randomUUID().toString())
                                            .code(voucherCode)
                                            .expiredDate(result.getFirst().getExpiredDate())
                                            .expiredPeriod(result.getFirst().getExpiredPeriod())
                                            .voucherTypeId(result.getFirst().getVoucherTypeId())
                                            .batchId(result.getFirst().getId())
                                            .state(NEW)
                                            .createAt(now)
                                            .createBy(result1.getT1().getUsername())
                                            .isNew(true)
                                            .build();
                                    voucherList.add(voucher);
                                }
                                return batchInsertVouchers(voucherList)
                                        .then(updateVoucherBatchState(result.getFirst().getId(),
                                                Constants.VOUCHER_BATCH_STATE.COMPLETE))
                                        .thenReturn(DataResponse.success("completed-to-insert-voucher"))
                                        .onErrorResume(throwable ->
                                                updateVoucherBatchState(result.getFirst().getId(), Constants.VOUCHER_BATCH_STATE.FAILED)
                                                        .thenReturn(DataResponse.failed("failed-to-insert-voucher"))
                                        );
                            }
                            return updateVoucherBatchState(result.getFirst().getId(), Constants.VOUCHER_BATCH_STATE.FAILED)
                                    .thenReturn(DataResponse.failed("voucher-type.error.not.exist"));
                        });
            }
            return updateVoucherBatchState(result.getFirst().getId(), Constants.VOUCHER_BATCH_STATE.FAILED)
                    .thenReturn(DataResponse.failed("voucher-batch.error.not.exist"));
        });
    }
}
