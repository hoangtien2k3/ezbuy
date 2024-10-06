package com.ezbuy.orderservice.controller;

import com.ezbuy.orderservice.service.InvoiceInfoService;
import com.ezbuy.ordermodel.constants.UrlPaths;
import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoRequest;
import com.ezbuy.ordermodel.dto.request.UpdateInvoiceInfoRequest;
import com.ezbuy.ordermodel.model.InvoiceInfo;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlPaths.InvoiceInfo.PRE_FIX)
public class InvoiceInfoController {

    private final InvoiceInfoService invoiceInfoService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse>> findInvoiceInfo(@RequestParam String userId, @RequestParam String organizationId) {
        return invoiceInfoService.getByUserIdAndOrganizationId(userId, organizationId)
                .map(result -> ResponseEntity.ok(new DataResponse<>("common.success", result)));
    }

    @PostMapping
    public Mono<DataResponse<InvoiceInfo>> createInvoiceInfo(@RequestBody CreateInvoiceInfoRequest request) {
        return invoiceInfoService.createInvoiceInfo(request).map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @PostMapping(UrlPaths.InvoiceInfo.ID)
    public Mono<DataResponse<InvoiceInfo>> updateInvoiceInfo(@PathVariable String id, @RequestBody UpdateInvoiceInfoRequest request) {
        return invoiceInfoService.updateInvoiceInfo(id, request)
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }
}
