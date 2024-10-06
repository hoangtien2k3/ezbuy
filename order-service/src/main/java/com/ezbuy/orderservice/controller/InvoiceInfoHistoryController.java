package com.ezbuy.orderservice.controller;

import com.ezbuy.ordermodel.constants.UrlPaths;
import com.ezbuy.ordermodel.dto.request.CreateInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
import com.ezbuy.ordermodel.model.InvoiceInfoHistory;
import com.ezbuy.orderservice.service.InvoiceInfoHistoryService;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(UrlPaths.InvoiceInfoHisTory.PRE_FIX)
public class InvoiceInfoHistoryController {

    private final InvoiceInfoHistoryService invoiceInfoHistoryService;

    @PostMapping
    public Mono<DataResponse<InvoiceInfoHistory>> createInvoiceInfoHistory(@RequestBody CreateInvoiceInfoHistoryRequest request) {
        return invoiceInfoHistoryService.createInvoiceInfoHistory(request).map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @GetMapping(UrlPaths.InvoiceInfoHisTory.GET)
    public Mono<DataResponse<DataResponse>> findInvoiceInfoHistory(GetInvoiceInfoHistoryRequest request) {
        return invoiceInfoHistoryService.findInvoiceInfoHistory(request).map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

}
