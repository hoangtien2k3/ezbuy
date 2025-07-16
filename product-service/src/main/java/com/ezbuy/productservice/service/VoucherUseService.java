package com.ezbuy.productservice.service;

import com.ezbuy.productmodel.dto.UpdateVoucherGiftRequest;
import com.ezbuy.productmodel.dto.UpdateVoucherPaymentRequest;
import com.ezbuy.productmodel.dto.request.CreateVoucherUseRequest;
import com.ezbuy.productmodel.model.VoucherUse;
import com.reactify.model.response.DataResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface VoucherUseService {

    Mono<DataResponse<VoucherUse>> createVoucherUse(CreateVoucherUseRequest request);

    Mono<DataResponse<List<VoucherUse>>> updateVoucherUse(CreateVoucherUseRequest request);

    Mono<DataResponse<List<VoucherUse>>> findVoucherUseByOrderId(String orderId);

    /**
     * Ham validate voucher da duoc su dung hay chua theo voucher code va nguoi nhan
     * voucher
     *
     * @param code
     * @param organizationId
     * @return
     */
    Mono<DataResponse<Boolean>> validateVoucherUsed(String code, String organizationId);

    /**
     * Ham update thong tin voucher use va voucher cho luong voucher tang khi thanh
     * toan
     *
     * @param request
     * @return
     */
    Mono<Boolean> updateVoucherGiftInfoByVoucherGiftCode(UpdateVoucherGiftRequest request);

    /**
     * Ham update thong tin voucher use va voucher trans cho luong su dung voucher
     * khi thanh toan
     *
     * @param request
     * @return
     */
    Mono<DataResponse<Boolean>> updateVoucherInfoPayment(UpdateVoucherPaymentRequest request);
}
