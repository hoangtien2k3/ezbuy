package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.CreateVoucherTransactionRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.model.VoucherTransaction;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface VoucherTransactionService {

    Mono<DataResponse<List<VoucherTransaction>>> findVoucherTransByVoucherIdAndStateNotIn(
            String voucherId, String state);

    Mono<DataResponse<VoucherTransaction>> createVoucherTransaction(CreateVoucherTransactionRequest request);

    Mono<DataResponse<String>> unlockVoucherTransaction(UnlockVoucherRequest unlockVoucherRequest);
}
