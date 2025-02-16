package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productmodel.dto.request.CreateVoucherTransactionRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.model.VoucherTransaction;
import com.ezbuy.productservice.service.VoucherTransactionService;
import com.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(UrlPaths.VoucherTransaction.PREFIX)
public class VoucherTransactionController {
    private final VoucherTransactionService voucherTransactionService;

    /**
     * ham luu thong tin voucherTransaction
     *
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.VoucherTransaction.CREATE)
    public Mono<DataResponse<VoucherTransaction>> createVoucherTransaction(
            @RequestBody CreateVoucherTransactionRequest request) {
        return voucherTransactionService.createVoucherTransaction(request);
    }
    /**
     * Ham thuc hien truy van va cap nhat inactive voucher transaction da het han
     *
     * @return
     */
    @PostMapping(value = UrlPaths.VoucherTransaction.UNLOCK)
    public Mono<DataResponse<String>> unlockVoucherTransaction(@RequestBody UnlockVoucherRequest unlockVoucherRequest) {
        return voucherTransactionService
                .unlockVoucherTransaction(unlockVoucherRequest)
                .map(rs -> rs);
    }
}
