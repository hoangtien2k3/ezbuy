package com.ezbuy.paymentservice.service;

import com.ezbuy.core.model.response.DataResponse;
import java.security.SignatureException;

import com.ezbuy.paymentservice.model.dto.request.PaymentResultRequest;
import com.ezbuy.paymentservice.model.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentservice.model.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentservice.model.dto.request.UpdateOrderStateRequest;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<DataResponse> createLinkCheckout(ProductPaymentRequest request) throws SignatureException;

    Mono<DataResponse> getResultFromVnPay(PaymentResultRequest request);

    Mono<DataResponse> updateOrderState(UpdateOrderStateRequest request);

    Mono<DataResponse> syncPaymentState(SyncOrderStateRequest request);
}
