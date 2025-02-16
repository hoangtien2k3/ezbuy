package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.request.GenVoucherRequest;
import com.ezbuy.productmodel.dto.request.SearchVoucherRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.dto.request.VoucherRequest;
import com.ezbuy.productmodel.dto.response.VoucherSearchResponse;
import com.ezbuy.productmodel.model.Voucher;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface VoucherService {
    // tim kiem voucher
    Mono<VoucherSearchResponse> searchVoucher(SearchVoucherRequest request);
    // tao moi voucher
    Mono<DataResponse<Voucher>> createVoucher(VoucherRequest request);
    // cap nhat voucher
    Mono<DataResponse<Boolean>> updateVoucher(String id, VoucherRequest request);

    /**
     * Ham cap nhat trang thai voucher theo voucher type id hoac voucher id
     *
     * @param voucherId
     * @param voucherTypeId
     * @param state
     * @return
     */
    Mono<DataResponse<Voucher>> updateStateVoucher(String voucherId, String voucherTypeId, String state);

    Mono<DataResponse<Voucher>> findVoucherNewByTypeCode(String code);

    Mono<DataResponse<Voucher>> findVoucherByCode(String code);

    /**
     * Ham thuc hien truy van mo lai voucher
     *
     * @param unlockVoucherRequest
     * @return
     */
    Mono<DataResponse<String>> unlockVoucher(UnlockVoucherRequest unlockVoucherRequest);

    /**
     * Ham thuc hien gen voucher tu dong theo voucher batch
     *
     * @param unlockVoucherRequest
     * @return
     */
    Mono<DataResponse<String>> genVoucher(GenVoucherRequest unlockVoucherRequest);

    Mono<DataResponse<String>> insertVoucher();
}
