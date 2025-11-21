package com.ezbuy.paymentservice.client;

import java.util.Optional;

import com.ezbuy.paymentservice.model.dto.request.UpdateOrderStatePayRequest;
import com.ezbuy.paymentservice.model.dto.response.MyPaymentDTO;
import reactor.core.publisher.Mono;

public interface PaymentClient {

    Mono<Optional<MyPaymentDTO>> searchPaymentState(String checkSum, String orderCode, String merchantCode);

    Mono<Optional<MyPaymentDTO>> updateOrderStateForPayment(UpdateOrderStatePayRequest request);
}
