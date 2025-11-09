package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productmodel.dto.UpdateVoucherGiftRequest;
import com.ezbuy.productmodel.dto.UpdateVoucherPaymentRequest;
import com.ezbuy.productmodel.dto.request.CreateVoucherUseRequest;
import com.ezbuy.productmodel.model.VoucherUse;
import com.ezbuy.productservice.service.VoucherUseService;
import com.ezbuy.core.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlPaths.VoucherUse.PREFIX)
public class VoucherUseController {
    private final VoucherUseService voucherUseService;

    /**
     * ham them moi thong tin voucher_use
     *
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.VoucherUse.CREATE)
    public Mono<DataResponse<VoucherUse>> createVoucherUse(@RequestBody CreateVoucherUseRequest request) {
        return voucherUseService.createVoucherUse(request);
    }

    /**
     * ham cap nhat thong tin voucher_use
     *
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.VoucherUse.UPDATE)
    public Mono<DataResponse<List<VoucherUse>>> updateVoucherUse(@RequestBody CreateVoucherUseRequest request) {
        return voucherUseService.updateVoucherUse(request);
    }

    /**
     * ham lay thong tin su dung voucher
     *
     * @param orderId
     *            ma voucher
     * @return
     */
    @GetMapping(value = UrlPaths.VoucherUse.GET_BY_SOURCE_ORDER_ID)
    public Mono<DataResponse<List<VoucherUse>>> findVoucherUseByOrderId(@RequestParam String orderId) {
        return voucherUseService.findVoucherUseByOrderId(orderId).map(rs -> rs);
    }

    /**
     * ham validate thong tin voucher co the su dung hay khong
     *
     * @param code
     *            ma voucher
     * @return
     */
    @GetMapping(value = UrlPaths.VoucherUse.VALIDATE_VOUCHER_USED)
    public Mono<DataResponse<Boolean>> validateVoucherUsed(
            @RequestParam String code, @RequestParam String organizationId) {
        return voucherUseService.validateVoucherUsed(code, organizationId).map(rs -> rs);
    }

    /**
     * ham lay thong tin voucher use theo voucher code
     *
     * @param request
     * @return
     */
    @PostMapping(value = UrlPaths.VoucherUse.VOUCHER_GIFT_BY_TYPE_CODE)
    public Mono<DataResponse<Boolean>> updateVoucherGiftInfoByVoucherGiftCode(
            @RequestBody UpdateVoucherGiftRequest request) {
        return voucherUseService
                .updateVoucherGiftInfoByVoucherGiftCode(request)
                .map(rs -> new DataResponse<>("common.success", rs));
    }

    /**
     * ham cap nhat thong tin voucher use và voucher transaction theo order id
     *
     * @param request
     * @return
     */
    @PostMapping(value = UrlPaths.VoucherUse.UPDATE_VOUCHER_INFO_PAYMENT)
    public Mono<DataResponse<Boolean>> updateVoucherInfoPayment(@RequestBody UpdateVoucherPaymentRequest request) {
        return voucherUseService.updateVoucherInfoPayment(request).map(rs -> rs);
    }
}
