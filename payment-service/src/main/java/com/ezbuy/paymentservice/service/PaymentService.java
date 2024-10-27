package com.ezbuy.paymentservice.service;

import com.ezbuy.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentmodel.dto.request.PaymentResultRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

import java.security.SignatureException;

public interface PaymentService {

    Mono<DataResponse> createLinkCheckout(ProductPaymentRequest request) throws SignatureException;

    Mono<DataResponse> getResultFromMyViettel(PaymentResultRequest request);

    Mono<DataResponse> updateOrderState(UpdateOrderStateRequest request);

    Mono<DataResponse> syncPaymentState(SyncOrderStateRequest request);

}
