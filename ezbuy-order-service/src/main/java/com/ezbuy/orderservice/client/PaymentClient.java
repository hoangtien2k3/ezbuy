package com.ezbuy.orderservice.client;

import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentmodel.dto.response.ProductPaymentResponse;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface PaymentClient {

    Mono<Optional<Long>> getTotalFee(ProductPriceRequest request);

    Mono<Optional<ProductPaymentResponse>> getLinkCheckOut(ProductPaymentRequest request);

    Mono<Optional<String>> updateOrderCode(UpdateOrderStateRequest request);
}
