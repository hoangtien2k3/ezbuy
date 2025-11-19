package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.request.UpdateOrderStatePayRequest;
import com.ezbuy.paymentmodel.dto.response.MyPaymentDTO;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface PaymentClient {

    Mono<Optional<MyPaymentDTO>> searchPaymentState(String checkSum, String orderCode, String merchantCode);

    Mono<Optional<MyPaymentDTO>> updateOrderStateForPayment(UpdateOrderStatePayRequest request);
}
