package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.CreateVoucherTransactionRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.model.VoucherTransaction;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VoucherTransactionService {
    /**
     * Ham tim kiem voucher tran theo voucher id va khong thuoc trang thai chi dinh
     * @param voucherId
     * @param state
     * @return
     */
    Mono<DataResponse<List<VoucherTransaction>>> findVoucherTransByVoucherIdAndStateNotIn(String voucherId, String state);
    Mono<DataResponse<VoucherTransaction>> createVoucherTransaction(CreateVoucherTransactionRequest request);

    /**
     * Ham thuc hien truy van va cap nhat inactive voucher transaction da het han
     * @param unlockVoucherRequest
     * @return
     */
    Mono<DataResponse> unlockVoucherTransaction(UnlockVoucherRequest unlockVoucherRequest);
}
