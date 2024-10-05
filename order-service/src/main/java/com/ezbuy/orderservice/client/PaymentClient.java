package com.ezbuy.orderservice.client;

import com.ezbuy.sme.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.sme.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.sme.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.sme.paymentmodel.dto.response.ProductPaymentResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface PaymentClient {

    Mono<Optional<Long>> getTotalFee(ProductPriceRequest request);

    Mono<Optional<ProductPaymentResponse>> getLinkCheckOut(ProductPaymentRequest request);

    Mono<Optional<String>> updateOrderCode(UpdateOrderStateRequest request);
}
