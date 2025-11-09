package com.ezbuy.orderservice.client.impl;

import com.ezbuy.orderservice.client.PaymentClient;
import com.ezbuy.paymentmodel.dto.request.ProductPaymentRequest;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStateRequest;
import com.ezbuy.paymentmodel.dto.response.ProductPaymentResponse;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class PaymentClientImpl implements PaymentClient {

    private final BaseRestClient baseRestClient;

    private final WebClient paymentClient;

    public PaymentClientImpl(BaseRestClient baseRestClient, @Qualifier("paymentClient") WebClient paymentClient) {
        this.baseRestClient = baseRestClient;
        this.paymentClient = paymentClient;
    }

    @Override
    public Mono<Optional<Long>> getTotalFee(ProductPriceRequest request) {
        return baseRestClient
                .post(paymentClient, "/v1/price/calculate", null, request, DataResponse.class)
                .flatMap(responseOptional -> {
                    Optional<DataResponse> dataResponseOptional = (Optional<DataResponse>) responseOptional;
                    if (dataResponseOptional.isEmpty()
                            || dataResponseOptional.get().getData() == null) {
                        return Mono.just(Optional.empty());
                    }

                    LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>)
                            dataResponseOptional.get().getData();
                    Long totalPrice = DataUtil.safeToLong(dataMap.get("totalPrice"));
                    return Mono.just(Optional.ofNullable(totalPrice));
                })
                .onErrorResume(throwable -> {
                    log.error("call ws payment getTotalFee error: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<Optional<ProductPaymentResponse>> getLinkCheckOut(ProductPaymentRequest request) {
        return baseRestClient
                .post(paymentClient, "/v1/payment/create-link-checkout", null, request, DataResponse.class)
                .flatMap(response -> {
                    Optional<DataResponse> responseOptional = (Optional<DataResponse>) response;
                    if (responseOptional.isEmpty() || responseOptional.get().getData() == null) {
                        return Mono.just(Optional.empty());
                    }

                    String data = DataUtil.safeToString(responseOptional.get().getData());
                    String dataJson =
                            DataUtil.parseObjectToString(responseOptional.get().getData());
                    ProductPaymentResponse paymentResponse =
                            DataUtil.parseStringToObject(dataJson, ProductPaymentResponse.class);
                    return Mono.just(Optional.ofNullable(paymentResponse));
                })
                .onErrorResume(throwable -> {
                    log.error("call ws payment createLinkCheckOut error: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }

    @Override
    public Mono<Optional<String>> updateOrderCode(UpdateOrderStateRequest request) {
        return getDataFromPost("/v1/payment/order-state", request);
    }

    private Mono<Optional<String>> getDataFromPost(String uri, Object request) {
        return baseRestClient
                .post(paymentClient, uri, null, request, DataResponse.class)
                .flatMap(response -> {
                    Optional<DataResponse> responseOptional = (Optional<DataResponse>) response;
                    if (responseOptional.isEmpty() || responseOptional.get().getData() == null) {
                        return Mono.just(Optional.empty());
                    }

                    String data = DataUtil.safeToString(responseOptional.get().getData());
                    return Mono.just(Optional.ofNullable(data));
                })
                .onErrorResume(throwable -> {
                    log.error("call ws payment createLinkCheckOut error: ", throwable);
                    return Mono.just(Optional.empty());
                });
    }
}
