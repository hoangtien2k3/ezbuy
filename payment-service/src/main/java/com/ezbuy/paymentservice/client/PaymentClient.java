package com.ezbuy.paymentservice.client;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateMyViettelRequest;
import com.ezbuy.paymentmodel.dto.response.MyPaymentDTO;
import java.util.Optional;
import reactor.core.publisher.Mono;

public interface PaymentClient {

    Mono<Optional<Long>> getTotalFee(ProductPriceRequest request);

    Mono<Optional<MyPaymentDTO>> searchPaymentState(String checkSum, String orderCode, String merchantCode);

    Mono<Optional<MyPaymentDTO>> updateOrderStateForMyViettel(UpdateOrderStateMyViettelRequest request);
}
