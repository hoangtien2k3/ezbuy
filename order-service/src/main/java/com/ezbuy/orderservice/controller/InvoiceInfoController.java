package com.ezbuy.orderservice.controller;

import com.ezbuy.orderservice.service.InvoiceInfoService;
import com.ezbuy.sme.authmodel.constants.AuthConstants;
import com.ezbuy.sme.authmodel.dto.request.EmployeeCreateRequest;
import com.ezbuy.sme.authmodel.dto.request.EmployeeUpdateRequest;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.ordermodel.constants.UrlPaths;
import com.ezbuy.sme.ordermodel.dto.request.CreateInvoiceInfoRequest;
import com.ezbuy.sme.ordermodel.dto.request.UpdateInvoiceInfoRequest;
import com.ezbuy.sme.ordermodel.model.InvoiceInfo;
import com.ezbuy.sme.settingmodel.model.NewsContent;
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
                .map(result -> ResponseEntity.ok(new DataResponse("common.success", result)));
    }

    @PostMapping
    public Mono<DataResponse<InvoiceInfo>> createInvoiceInfo(@RequestBody CreateInvoiceInfoRequest request) {
        return invoiceInfoService.createInvoiceInfo(request).map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }

    @PostMapping(UrlPaths.InvoiceInfo.ID)
    public Mono<DataResponse<InvoiceInfo>> updateInvoiceInfo(@PathVariable String id, @RequestBody UpdateInvoiceInfoRequest request) {
        return invoiceInfoService.updateInvoiceInfo(id, request).map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }
}
