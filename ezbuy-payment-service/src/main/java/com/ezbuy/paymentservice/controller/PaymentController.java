package com.ezbuy.paymentservice.controller;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentmodel.constants.UrlPaths;
import com.ezbuy.paymentmodel.dto.request.PaymentResultRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentservice.service.PaymentService;
import com.reactify.model.response.DataResponse;
import java.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.Payment.PREFIX)
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(UrlPaths.Payment.CREATE_LINK_CHECKOUT)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> createLinkCheckout(@RequestBody ProductPaymentRequest request)
            throws SignatureException {
        return paymentService.createLinkCheckout(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Payment.PAYMENT_RESULT)
    @RequestMapping(
            path = UrlPaths.Payment.PAYMENT_RESULT,
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<ResponseEntity<DataResponse>> getResultFromMyPayment(PaymentResultRequest request) {
        return paymentService.getResultFromVnPay(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Payment.ORDER_STATE)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> updateOrderState(@RequestBody UpdateOrderStateRequest request) {
        return paymentService.updateOrderState(request).map(ResponseEntity::ok);
    }

    @PostMapping(UrlPaths.Payment.SYNC_PAYMENT)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<ResponseEntity<DataResponse>> syncPaymentState(@RequestBody SyncOrderStateRequest request) {
        return paymentService.syncPaymentState(request).map(ResponseEntity::ok);
    }
}
