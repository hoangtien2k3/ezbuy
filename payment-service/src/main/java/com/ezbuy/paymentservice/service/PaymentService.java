package com.ezbuy.paymentservice.service;

import com.viettel.sme.framework.model.response.DataResponse;
import com.viettel.sme.ordermodel.dto.request.CreateOrderPaidRequest;
import com.viettel.sme.ordermodel.dto.request.SyncOrderStateRequest;
import com.ezbuy.paymentmodel.dto.request.PaymentResultRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import reactor.core.publisher.Mono;

import java.security.SignatureException;

public interface PaymentService {

    Mono<DataResponse> createLinkCheckout(ProductPaymentRequest request) throws SignatureException;

    Mono<DataResponse> getResultFromMyViettel(PaymentResultRequest request);

    Mono<DataResponse> updateOrderState(UpdateOrderStateRequest request);

    Mono<DataResponse> syncPaymentState(SyncOrderStateRequest request);


}
