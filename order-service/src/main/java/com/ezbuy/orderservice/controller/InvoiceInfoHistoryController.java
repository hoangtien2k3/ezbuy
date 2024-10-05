package com.ezbuy.orderservice.controller;

import com.ezbuy.orderservice.service.InvoiceInfoHistoryService;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.ordermodel.constants.UrlPaths;
import com.ezbuy.sme.ordermodel.dto.request.CreateInvoiceInfoHistoryRequest;
import com.ezbuy.sme.ordermodel.model.InvoiceInfoHistory;
import com.ezbuy.sme.ordermodel.dto.request.GetInvoiceInfoHistoryRequest;
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
