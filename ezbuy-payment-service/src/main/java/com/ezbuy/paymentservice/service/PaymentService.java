package com.ezbuy.paymentservice.service;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentmodel.dto.request.PaymentResultRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.reactify.model.response.DataResponse;
import java.security.SignatureException;
import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<DataResponse> createLinkCheckout(ProductPaymentRequest request) throws SignatureException;

    Mono<DataResponse> getResultFromVnPay(PaymentResultRequest request);

    Mono<DataResponse> updateOrderState(UpdateOrderStateRequest request);

    Mono<DataResponse> syncPaymentState(SyncOrderStateRequest request);
}
