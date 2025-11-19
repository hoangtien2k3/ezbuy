package com.ezbuy.productservice.service.impl;

import static com.ezbuy.ordermodel.constants.Constants.SYSTEM;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.productmodel.constants.Constants.ORDER_SYSTEM;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_STATE.LOCKED;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_STATE.NEW;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_USE_STATE.ACTIVE;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_USE_STATE.PRE_ACTIVE;

import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.dto.UpdateVoucherGiftRequest;
import com.ezbuy.productmodel.dto.UpdateVoucherPaymentRequest;
import com.ezbuy.productmodel.dto.request.CreateVoucherUseRequest;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productmodel.model.VoucherTransaction;
import com.ezbuy.productmodel.model.VoucherUse;
import com.ezbuy.productservice.client.AuthClient;
import com.ezbuy.productservice.repository.VoucherRepository;
import com.ezbuy.productservice.repository.VoucherTransactionRepository;
import com.ezbuy.productservice.repository.VoucherUseRepository;
import com.ezbuy.productservice.service.VoucherTransactionService;
import com.ezbuy.productservice.service.VoucherUseService;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.SecurityUtils;
import com.ezbuy.core.util.Translator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherUseServiceImpl implements VoucherUseService {
    private final VoucherUseRepository voucherUseRepository;
    private final VoucherTransactionService voucherTransactionService;
    private final VoucherRepository voucherRepository;
    private final VoucherTransactionRepository voucherTransactionRepository;
    private final AuthClient authClient;

    @Override
    @Transactional
    public Mono<DataResponse<VoucherUse>> createVoucherUse(CreateVoucherUseRequest request) {
        String state = DataUtil.safeTrim(request.getState());
        if (DataUtil.isNullOrEmpty(state)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.empty"));
        }
        if (!PRE_ACTIVE.equals(state)
                && !Constants.VOUCHER_USE_STATE.INACTIVE.equals(state)
                && !ACTIVE.equals(state)
                && !Constants.VOUCHER_USE_STATE.USED.equals(state)) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.use.state.is.valid"));
        }
        return voucherUseRepository.getSysDate().flatMap(getSysDate -> {
            String voucherUseId = UUID.randomUUID().toString();
            String systemType = DataUtil.safeTrim(request.getSystemType());
            VoucherUse voucherUse = VoucherUse.builder()
                    .id(voucherUseId)
                    .voucherId(request.getVoucherId())
                    .userId(request.getUserId())
                    .username(request.getUsename())
                    .systemType(systemType)
                    .createDate(getSysDate)
                    .expiredDate(request.getExpiredDate())
                    .state(state)
                    .sourceOrderId(request.getSourceOrderId())
                    .createAt(getSysDate)
                    .createBy(request.getUsename())
                    .updateAt(getSysDate)
                    .updateBy(request.getUsename())
                    .isNew(true)
                    .build();
            return voucherUseRepository
                    .save(voucherUse)
                    .switchIfEmpty(Mono.error(new BusinessException(
                            CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocale("voucher.user.insert.failed"))))
                    .flatMap(x -> Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), voucherUse)));
        });
    }

    @Override
    @Transactional
    public Mono<DataResponse<List<VoucherUse>>> updateVoucherUse(CreateVoucherUseRequest request) {
        String orderId = DataUtil.safeTrim(request.getSourceOrderId());
        if (DataUtil.isNullOrEmpty(orderId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.order.id.empty"));
        }
        String state = DataUtil.safeTrim(request.getState());
        if (DataUtil.isNullOrEmpty(state)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.empty"));
        }
        if (!PRE_ACTIVE.equals(state)
                && !Constants.VOUCHER_USE_STATE.INACTIVE.equals(state)
                && !ACTIVE.equals(state)
                && !Constants.VOUCHER_USE_STATE.USED.equals(state)) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.use.state.is.valid"));
        }
        var getSysdate = voucherUseRepository.getSysDate();
        return Mono.zip(
                        voucherUseRepository
                                .findVoucherUseByOrderId(orderId)
                                .collectList()
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        CommonErrorCode.NOT_FOUND,
                                        Translator.toLocale("voucher.by.voucher.type.id.empty")))),
                        getSysdate)
                .flatMap(response -> {
                    LocalDateTime now = response.getT2();
                    List<VoucherUse> voucherUseList = response.getT1();
                    voucherUseList.forEach(voucherUse -> {
                        voucherUse.setState(state);
                        voucherUse.setExpiredDate(request.getExpiredDate());
                        voucherUse.setUpdateAt(now);
                        voucherUse.setUpdateBy("system");
                    });
                    return voucherUseRepository
                            .saveAll(voucherUseList)
                            .collectList()
                            .flatMap(updateVoucherUse ->
                                    Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), updateVoucherUse)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<List<VoucherUse>>> findVoucherUseByOrderId(String orderId) {
        String sourceOrderId = DataUtil.safeTrim(orderId);
        if (DataUtil.isNullOrEmpty(sourceOrderId)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.use.error.order.id.empty"));
        }
        return voucherUseRepository
                .findVoucherUseByOrderId(orderId)
                .collectList()
                .switchIfEmpty(Mono.just(Collections.emptyList()))
                .flatMap(voucherUseList ->
                        Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), voucherUseList)));
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> validateVoucherUsed(String code, String organizationId) {
        String voucherCode = DataUtil.safeTrim(code);
        if (DataUtil.isNullOrEmpty(voucherCode)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucher.code.empty"));
        }
        return SecurityUtils.getCurrentUser().flatMap(user -> Mono.zip(
                        voucherRepository
                                .findVoucherByVoucherCodeAndState(voucherCode, Constants.VOUCHER_STATE.USED)
                                .collectList(),
                        authClient.findIndividualByUserIdAndOrganizationId(user.getId(), organizationId))
                .flatMap(voucherIndividual -> {
                    if (DataUtil.isNullOrEmpty(voucherIndividual.getT1())) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.error.voucher.not.exist"));
                    }
                    String voucherId =
                            DataUtil.safeTrim(voucherIndividual.getT1().getFirst().getId());
                    return voucherUseRepository
                            .findVoucherUsedByVoucherIdAndUserId(
                                    voucherId, voucherIndividual.getT2().getId())
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.error.voucher.not.exist")))
                            .flatMap(voucherUsed -> {
                                // query bang voucher_transaction neu co ban ghi bao loi
                                return voucherTransactionService
                                        .findVoucherTransByVoucherIdAndStateNotIn(
                                                voucherId, Constants.VOUCHER_TRANSACTION_STATE.INACTIVE)
                                        .flatMap(voucherTransaction -> {
                                            if (DataUtil.isNullOrEmpty(voucherTransaction.getErrorCode())
                                                    && !DataUtil.isNullOrEmpty(voucherTransaction.getData())) {
                                                return Mono.error(new BusinessException(
                                                        CommonErrorCode.INVALID_PARAMS,
                                                        "voucher.error.voucher.not.exist"));
                                            }
                                            return Mono.just(
                                                    new DataResponse<>(null, Translator.toLocaleVi("success"), true));
                                        })
                                        .onErrorResume(throwable -> Mono.error(new BusinessException(
                                                CommonErrorCode.INTERNAL_SERVER_ERROR,
                                                DataUtil.isNullOrEmpty(throwable.getMessage())
                                                        ? "common.error"
                                                        : throwable.getMessage())));
                            })
                            .onErrorResume(throwable -> Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                    DataUtil.isNullOrEmpty(throwable.getMessage())
                                            ? "common.error"
                                            : throwable.getMessage())));
                })
                .onErrorResume(throwable -> Mono.error(new BusinessException(
                        CommonErrorCode.INTERNAL_SERVER_ERROR,
                        !DataUtil.isNullOrEmpty(throwable.getMessage()) ? throwable.getMessage() : "common.error"))));
    }

    @Override
    @Transactional
    public Mono<Boolean> updateVoucherGiftInfoByVoucherGiftCode(UpdateVoucherGiftRequest request) {
        String orderId = DataUtil.safeTrim(request.getOrderId());
        if (DataUtil.isNullOrEmpty(orderId)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.voucher.code.empty"));
        }
        if (DataUtil.isNullOrEmpty(request.getOrderState())) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.order.state.empty"));
        }
        if (!List.of(3, 4).contains(request.getOrderState())) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.order.state.invalid"));
        }
        // lay thong tin voucher use theo order_id va state preActive
        return voucherUseRepository
                .findVoucherUseByOrderIdAndState(orderId, PRE_ACTIVE)
                .switchIfEmpty(Mono.error(
                        new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.use.error.not.exist")))
                .collectList()
                .flatMap(voucherUseList -> {
                    List<String> voucherIdList = voucherUseList.stream()
                            .map(VoucherUse::getVoucherId)
                            .collect(Collectors.toList());
                    return Mono.zip(
                                    voucherRepository
                                            .findVoucherByVoucherIdListAndState(voucherIdList, LOCKED)
                                            .switchIfEmpty(Mono.error(new BusinessException(
                                                    CommonErrorCode.INTERNAL_SERVER_ERROR, "voucher.not.exist")))
                                            .collectList(),
                                    voucherRepository.getSysDate())
                            .flatMap(voucherSysDate -> {
                                LocalDateTime sysDate = voucherSysDate.getT2();
                                Flux<Voucher> updateVoucher = Flux.fromIterable(Collections.emptyList());
                                Flux<VoucherUse> updateVoucherUse = Flux.fromIterable(Collections.emptyList());
                                List<Voucher> voucherList = voucherSysDate.getT1();
                                // case hoa don hoan thanh
                                if (DataUtil.safeEqual(request.getOrderState(), 3)) {
                                    // cap nhat thong tin voucher state = used
                                    voucherList.forEach(voucher -> {
                                        voucher.setNew(false);
                                        voucher.setState(Constants.VOUCHER_STATE.USED);
                                        voucher.setUpdateAt(sysDate);
                                        voucher.setUpdateBy(ORDER_SYSTEM);
                                    });
                                    updateVoucher = voucherRepository.saveAll(voucherList);
                                    // cap nhat thong tin voucher use state = active
                                    voucherUseList.forEach(voucherUse -> {
                                        voucherUse.setNew(false);
                                        voucherUse.setState(ACTIVE);
                                        Voucher voucher = voucherList.stream()
                                                .filter(item ->
                                                        DataUtil.safeEqual(item.getId(), voucherUse.getVoucherId()))
                                                .findFirst()
                                                .orElse(new Voucher());
                                        if (!DataUtil.isNullOrEmpty(voucher.getExpiredDate())) {
                                            voucherUse.setExpiredDate(voucher.getExpiredDate());
                                        } else if (!DataUtil.isNullOrEmpty(voucher.getExpiredPeriod())) {
                                            LocalDateTime expiredDate = sysDate.plusDays(voucher.getExpiredPeriod());
                                            voucherUse.setExpiredDate(expiredDate);
                                        }
                                        voucherUse.setUpdateAt(sysDate);
                                        voucherUse.setUpdateBy(ORDER_SYSTEM);
                                    });
                                    updateVoucherUse = voucherUseRepository.saveAll(voucherUseList);
                                } else if (DataUtil.safeEqual(request.getOrderState(), 4)) {
                                    // cap nhat thong tin voucher state = new
                                    voucherList.forEach(voucher -> {
                                        voucher.setNew(false);
                                        voucher.setState(NEW);
                                        voucher.setUpdateAt(sysDate);
                                        voucher.setUpdateBy(ORDER_SYSTEM);
                                    });
                                    updateVoucher = voucherRepository.saveAll(voucherList);
                                    // cap nhat thong tin voucher use state = inActive
                                    voucherUseList.forEach(voucherUse -> {
                                        voucherUse.setNew(false);
                                        voucherUse.setState(Constants.VOUCHER_USE_STATE.INACTIVE);
                                        voucherUse.setUpdateAt(sysDate);
                                        voucherUse.setUpdateBy(ORDER_SYSTEM);
                                    });
                                    updateVoucherUse = voucherUseRepository.saveAll(voucherUseList);
                                }
                                return Mono.zip(updateVoucher.collectList(), updateVoucherUse.collectList())
                                        .onErrorResume(throwable -> Mono.error(new BusinessException(
                                                CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")))
                                        .map(update -> true);
                            });
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<Boolean>> updateVoucherInfoPayment(UpdateVoucherPaymentRequest request) {
        // validate request
        String orderId = DataUtil.safeTrim(request.getSourceOrderId());
        if (DataUtil.isNullOrEmpty(orderId)) {
            return Mono.error(new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.error.order.id.empty"));
        }
        String voucherUseState = DataUtil.safeTrim(request.getVoucherUseState());
        String voucherTransState = DataUtil.safeTrim(request.getVoucherTransState());
        if (DataUtil.isNullOrEmpty(voucherTransState)
                || !List.of(
                                Constants.VOUCHER_TRANSACTION_STATE.USED,
                                Constants.VOUCHER_TRANSACTION_STATE.INACTIVE,
                                Constants.VOUCHER_TRANSACTION_STATE.ACTIVE)
                        .contains(voucherTransState)) {
            return Mono.error(new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, "voucher.use.error.voucher.trans.state.invalid"));
        }
        // lay ra cac ban ghi voucher transaction hop le
        Flux<VoucherTransaction> voucherTrans;
        if (DataUtil.safeEqual(request.getIsOrderHistory(), true)) { // neu cap nhat trang thai don hang
            voucherTrans = voucherTransactionRepository.findVoucherTransByOrderIdAndState(
                    orderId, Constants.VOUCHER_TRANSACTION_STATE.ACTIVE);
        } else { // neu cap nhat trang thai thanh toan
            voucherTrans = voucherTransactionRepository.findVoucherTransByOrderIdAndState(
                    orderId, Constants.VOUCHER_TRANSACTION_STATE.PRE_ACTIVE);
        }
        return Mono.zip(voucherTrans.collectList(), voucherUseRepository.getSysDate())
                .flatMap(voucherTransSysDate -> {
                    List<VoucherTransaction> voucherTransactionList = voucherTransSysDate.getT1();
                    if (DataUtil.isNullOrEmpty(voucherTransactionList)) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.NOT_EXIST_DATA, "voucher.use.error.info.not.found"));
                    }
                    List<Mono<DataResponse>> handleUpdate = new ArrayList<>();
                    // update voucher use & voucher trans
                    voucherTransactionList.forEach(
                            voucherTransaction -> handleUpdate.add(handleUpdateVoucherTransAndVoucherUse(
                                    voucherTransaction,
                                    request.getIsOrderHistory(),
                                    voucherTransSysDate.getT2(),
                                    voucherUseState,
                                    voucherTransState)));
                    return Flux.concat(handleUpdate)
                            .collectList()
                            .map(update -> new DataResponse<>("common.success", true));
                });
    }

    private Mono<DataResponse> handleUpdateVoucherTransAndVoucherUse(
            VoucherTransaction voucherTransaction,
            Boolean isOrderHistory,
            LocalDateTime updateAt,
            String voucherUseState,
            String voucherTransState) {
        String voucherId = voucherTransaction.getVoucherId();
        Flux<VoucherUse> voucherUseFLux;
        if (DataUtil.safeEqual(isOrderHistory, true)) {
            voucherUseFLux = voucherUseRepository.findVoucherUseByVoucherIdAndState(
                    voucherId, Constants.VOUCHER_USE_STATE.USING);
        } else {
            voucherUseFLux = voucherUseRepository.findVoucherUseByVoucherIdAndState(voucherId, ACTIVE);
        }
        return voucherUseFLux.collectList().flatMap(voucherUseList -> {
            if (DataUtil.isNullOrEmpty(voucherUseList)) {
                return Mono.error(
                        new BusinessException(CommonErrorCode.NOT_EXIST_DATA, "voucher.use.error.info.not.found"));
            }
            String username = DataUtil.safeEqual(isOrderHistory, true) ? ORDER_SYSTEM : SYSTEM;

            Mono<VoucherUse> updateVoucherUse;
            if (DataUtil.safeEqual(isOrderHistory, true)
                    && !DataUtil.safeEqual(voucherUseState, Constants.VOUCHER_USE_STATE.USED)
                    && !DataUtil.safeEqual(voucherUseState, ACTIVE)) {
                updateVoucherUse = Mono.just(new VoucherUse());
            } else {
                VoucherUse voucherUse = voucherUseList.getFirst();
                voucherUse.setState(voucherUseState);
                voucherUse.setUpdateAt(updateAt);
                voucherUse.setUpdateBy(username);
                voucherUse.setNew(false);
                updateVoucherUse = voucherUseRepository.save(voucherUse);
            }
            // update voucher trans
            voucherTransaction.setState(voucherTransState);
            voucherTransaction.setUpdateAt(updateAt);
            voucherTransaction.setUpdateBy(username);
            voucherTransaction.setNew(false);
            Mono<VoucherTransaction> updateVoucherTrans = voucherTransactionRepository.save(voucherTransaction);

            return Mono.zip(updateVoucherUse, updateVoucherTrans)
                    .map(update -> new DataResponse<>("common.success", true))
                    .onErrorResume(throwable ->
                            Mono.error(new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")));
        });
    }
}
