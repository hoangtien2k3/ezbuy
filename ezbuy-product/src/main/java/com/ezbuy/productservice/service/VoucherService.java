package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.GenVoucherRequest;
import com.ezbuy.productmodel.dto.request.SearchVoucherRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.dto.request.VoucherRequest;
import com.ezbuy.productmodel.dto.response.VoucherSearchResponse;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface VoucherService {

    Mono<VoucherSearchResponse> searchVoucher(SearchVoucherRequest request);

    Mono<DataResponse<Voucher>> createVoucher(VoucherRequest request);

    Mono<DataResponse<Boolean>> updateVoucher(String id, VoucherRequest request);

    Mono<DataResponse<Voucher>> updateStateVoucher(String voucherId, String voucherTypeId, String state);

    Mono<DataResponse<Voucher>> findVoucherNewByTypeCode(String code);

    Mono<DataResponse<Voucher>> findVoucherByCode(String code);

    Mono<DataResponse<String>> unlockVoucher(UnlockVoucherRequest unlockVoucherRequest);

    Mono<DataResponse<String>> genVoucher(GenVoucherRequest unlockVoucherRequest);

    Mono<DataResponse<String>> insertVoucher();
}
