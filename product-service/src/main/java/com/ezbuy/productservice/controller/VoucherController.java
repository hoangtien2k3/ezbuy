package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productmodel.dto.request.*;
import com.ezbuy.productmodel.dto.response.VoucherSearchResponse;
import com.ezbuy.productmodel.model.Voucher;
import com.ezbuy.productservice.service.VoucherService;
import com.reactify.constants.MessageConstant;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.ezbuy.productmodel.constants.UrlPaths.DEFAULT_V1_PREFIX;
import static com.ezbuy.productmodel.constants.UrlPaths.Voucher.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(DEFAULT_V1_PREFIX)
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping(value = SEARCH_VOUCHER)
    public Mono<ResponseEntity<DataResponse<VoucherSearchResponse>>> searchVoucher(SearchVoucherRequest request) {
    return voucherService.searchVoucher(request)
        .map(rs -> ResponseEntity.ok(
            new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), rs)));
    }

    @PostMapping(value = CREATE_VOUCHER)
    public Mono<DataResponse<Voucher>> createVoucher(@Valid @RequestBody VoucherRequest request) {
    return voucherService.createVoucher(request);
    }

    @PutMapping(value =UPDATE_VOUCHER )
    public Mono<DataResponse<Boolean>> updateVoucher(@PathVariable String id,@Valid @RequestBody VoucherRequest request) {
    return voucherService.updateVoucher(id,request);
    }

    /**
     * ham cap nhat thong tin voucher
     *
     * @param request
     * @return
     */
    @PostMapping(UrlPaths.Voucher.UPDATE_STATE)
    public Mono<DataResponse<Voucher>> updateStateVoucher(@RequestBody CreateVoucherRequest request) {
        return voucherService.updateStateVoucher(request.getVoucherId(), request.getVoucherTypeId(), request.getState());
    }

    /**
     * ham lay thong tin voucher hoat dong
     *
     * @param code ma voucher type
     * @return
     */
    @GetMapping(value = UrlPaths.Voucher.NEW_BY_CODE)
    public Mono<DataResponse<Voucher>> findVoucherNewByTypeCode(@RequestParam String code) {
        return voucherService.findVoucherNewByTypeCode(code).map(rs -> rs);
    }

    /**
     * ham lay thong tin voucher hoat dong
     *
     * @param code ma voucher type
     * @return
     */
    @GetMapping(value = UrlPaths.Voucher.GET_BY_CODE)
    public Mono<DataResponse<Voucher>> findVoucherByCode(@RequestParam String code) {
        return voucherService.findVoucherByCode(code).map(rs -> rs);
    }

    /**
     * ham thuc hien truy van mo lai voucher
     *
     * @return
     */
    @PostMapping(value = UrlPaths.Voucher.UNLOCK_VOUCHER)
    public Mono<DataResponse<String>> unlockVoucher(@RequestBody UnlockVoucherRequest unlockVoucherRequest) {
        return voucherService.unlockVoucher(unlockVoucherRequest).map(rs -> rs);
    }

    /**
     * ham thuc hien truy van mo lai voucher
     *
     * @return
     */
    @PostMapping(value = UrlPaths.Voucher.GEN_VOUCHER)
    public Mono<DataResponse<String>> genVoucher(@RequestBody GenVoucherRequest genVoucherRequest) {
        return voucherService.genVoucher(genVoucherRequest).map(rs -> rs);
    }

    @PostMapping(value = UrlPaths.Voucher.INSERT_VOUCHER)
    public Mono<DataResponse<String>> insertVoucher() {
        return voucherService.insertVoucher().map(rs -> rs);
    }
}
