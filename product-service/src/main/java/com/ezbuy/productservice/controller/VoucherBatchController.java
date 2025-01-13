package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.dto.request.CreateVoucherBatchRequest;
import com.ezbuy.productmodel.dto.request.VoucherBatchRequest;
import com.ezbuy.productmodel.model.VoucherBatch;
import com.ezbuy.productservice.service.VoucherBatchService;
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
import static com.ezbuy.productmodel.constants.UrlPaths.VoucherBatch.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(DEFAULT_V1_PREFIX)
public class VoucherBatchController {

    private final VoucherBatchService voucherBatchService;

    @GetMapping(value = GET_ALL_VOUCHER_BATCH)
    public Mono<ResponseEntity<DataResponse>> getAllVoucherBatch() {
        return voucherBatchService.getAllVoucherBatch()
                .map(result -> ResponseEntity.ok(new DataResponse(Translator.toLocaleVi(
                        MessageConstant.SUCCESS), result)));
    }

    @PostMapping(value = CREATE_VOUCHER_BATCH)
    public Mono<DataResponse> createVoucherBatch(@Valid @RequestBody CreateVoucherBatchRequest request) {
        return voucherBatchService.createVoucherBatch(request)
                .map(result -> new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), result));
    }

    @PutMapping(value = UPDATE_VOUCHER_BATCH)
    public Mono<DataResponse> updateVoucherBatch(@PathVariable String id, @Valid @RequestBody CreateVoucherBatchRequest request) {
        return voucherBatchService.updateVoucherBatch(id, request)
                .map(result -> new DataResponse<>(Translator.toLocaleVi(MessageConstant.SUCCESS), result));
    }

    @GetMapping(value = DETAIL_VOUCHER_BATCH)
    public Mono<DataResponse<VoucherBatch>> getInfoVoucherBatch(@PathVariable("id") String id) {
        return voucherBatchService.getVoucherBatch(id);
    }

    @GetMapping(value = GET_ALL_VOUCHER_TYPE)
    public Mono<ResponseEntity<DataResponse>> getAllVoucherType() {
        return voucherBatchService.getAllVoucherType()
                .map(result -> ResponseEntity.ok(new DataResponse(Translator.toLocaleVi(
                        MessageConstant.SUCCESS), result)));
    }

    @GetMapping(value = SEARCH_VOUCHER_BATCH)
    public Mono<ResponseEntity<DataResponse>> searchVoucherBatch(VoucherBatchRequest request) {
        return voucherBatchService.searchVoucherBatch(request)
                .map(rs -> ResponseEntity.ok(
                        new DataResponse(Translator.toLocaleVi(MessageConstant.SUCCESS), rs)));
    }

}