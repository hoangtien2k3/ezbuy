package com.ezbuy.productservice.service.impl;

import static com.ezbuy.ordermodel.constants.Constants.SYSTEM;
import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;
import static com.ezbuy.productmodel.constants.Constants.VOUCHER_STATE.INACTIVE;

import com.ezbuy.productmodel.constants.Constants;
import com.ezbuy.productmodel.dto.request.CreateVoucherTransactionRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.model.VoucherTransaction;
import com.ezbuy.productservice.repository.VoucherTransactionRepository;
import com.ezbuy.productservice.service.VoucherTransactionService;
import com.reactify.constants.CommonErrorCode;
import com.reactify.exception.BusinessException;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
import com.reactify.util.SecurityUtils;
import com.reactify.util.Translator;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class VoucherTransactionServiceImpl implements VoucherTransactionService {
    private final VoucherTransactionRepository voucherTransactionRepository;

    @Override
    public Mono<DataResponse<List<VoucherTransaction>>> findVoucherTransByVoucherIdAndStateNotIn(
            String voucherId, String state) {
        voucherId = DataUtil.safeTrim(voucherId);
        if (DataUtil.isNullOrEmpty(voucherId)) {
            return Mono.error(
                    new BusinessException(CommonErrorCode.INVALID_PARAMS, "voucher.transaction.voucher.id.empty"));
        }
        return voucherTransactionRepository
                .findVoucherTransByVoucherIdAndStateNotIn(voucherId, state)
                .collectList()
                .flatMap(response -> Mono.just(new DataResponse<>(Translator.toLocale("Success"), response)));
    }

    @Override
    @Transactional
    public Mono<DataResponse<VoucherTransaction>> createVoucherTransaction(CreateVoucherTransactionRequest request) {
        String state = DataUtil.safeTrim(request.getState());
        if (DataUtil.isNullOrEmpty(state)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.empty"));
        }
        if (!Constants.VOUCHER_TRANSACTION_STATE.PRE_ACTIVE.equals(state)
                && !Constants.VOUCHER_TRANSACTION_STATE.INACTIVE.equals(state)
                && !Constants.VOUCHER_TRANSACTION_STATE.ACTIVE.equals(state)
                && !Constants.VOUCHER_TRANSACTION_STATE.USED.equals(state)) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.use.state.is.valid"));
        }
        String transactionType = DataUtil.safeTrim(request.getTransactionType());
        if (DataUtil.isNullOrEmpty(transactionType)) {
            throw new BusinessException(CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.state.empty"));
        }
        if (!Constants.VOUCHER_TRANSACTION_TYPE.CONNECT.equals(transactionType)
                && !Constants.VOUCHER_TRANSACTION_TYPE.AFTER.equals(transactionType)) {
            throw new BusinessException(
                    CommonErrorCode.INVALID_PARAMS, Translator.toLocale("voucher.transaction.type.is.valid"));
        }
        return Mono.zip(voucherTransactionRepository.getSysDate(), SecurityUtils.getCurrentUser())
                .flatMap(sysDateUser -> {
                    String id = UUID.randomUUID().toString();
                    VoucherTransaction voucherTransaction = VoucherTransaction.builder()
                            .id(id)
                            .voucherId(request.getVoucherId())
                            .userId(request.getUserId())
                            .transactionCode(request.getSourceOrderId())
                            .transactionDate(sysDateUser.getT1())
                            .transactionType(transactionType)
                            .amount(request.getAmount())
                            .state(state)
                            .createBy(sysDateUser.getT2().getUsername())
                            .updateBy(sysDateUser.getT2().getUsername())
                            .createAt(sysDateUser.getT1())
                            .updateAt(sysDateUser.getT1())
                            .isNew(true)
                            .build();
                    return voucherTransactionRepository
                            .save(voucherTransaction)
                            .switchIfEmpty(Mono.error(new BusinessException(
                                    CommonErrorCode.INTERNAL_SERVER_ERROR,
                                    Translator.toLocale("voucher.transaction.insert.failed"))))
                            .flatMap(x ->
                                    Mono.just(new DataResponse<>(Translator.toLocaleVi(SUCCESS), voucherTransaction)));
                });
    }

    @Override
    @Transactional
    public Mono<DataResponse<String>> unlockVoucherTransaction(UnlockVoucherRequest unlockVoucherRequest) {
        // lay danh sach voucher transaction het han
        return Mono.zip(voucherTransactionRepository.getAllExpiredVoucherTransaction(unlockVoucherRequest.getExpiredMinutes()).collectList(),
                        voucherTransactionRepository.getSysDate())
                .flatMap(voucherTransactionListSysDate -> {
                    List<VoucherTransaction> voucherTransactionList = voucherTransactionListSysDate.getT1();
                    LocalDateTime now = voucherTransactionListSysDate.getT2();
                    // update inactive voucher transaction
                    Flux<VoucherTransaction> updateVoucherTransaction;
                    if (!DataUtil.isNullOrEmpty(voucherTransactionList)) {
                        voucherTransactionList.forEach(voucherTrans -> {
                            voucherTrans.setNew(false);
                            voucherTrans.setState(INACTIVE);
                            voucherTrans.setUpdateBy(SYSTEM);
                            voucherTrans.setUpdateAt(now);
                        });
                        updateVoucherTransaction = voucherTransactionRepository.saveAll(voucherTransactionList);
                    } else {
                        updateVoucherTransaction = Flux.fromIterable(new ArrayList<>());
                    }
                    return updateVoucherTransaction
                            .collectList()
                            .flatMap(update -> Mono.just(
                                    DataResponse.success(Translator.toLocale("voucher.transaction.unlock.completed"))))
                            .onErrorResume(throwable -> Mono.error(
                                    new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "common.error")));
                });
    }
}
