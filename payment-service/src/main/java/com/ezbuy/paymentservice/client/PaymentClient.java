package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateMyViettelRequest;
import com.ezbuy.paymentmodel.dto.response.MyViettelDTO;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PaymentClient {

    Mono<Optional<Long>> getTotalFee(ProductPriceRequest request);

    Mono<Optional<MyViettelDTO>> searchPaymentState(String checkSum, String orderCode, String merchantCode);

    Mono<Optional<MyViettelDTO>> updateOrderStateForMyViettel(UpdateOrderStateMyViettelRequest request);
}
