package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.paymentmodel.dto.request.UpdateOrderStatePayRequest;
import com.ezbuy.paymentmodel.dto.response.MyPaymentDTO;
import com.ezbuy.paymentservice.client.PaymentClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@DependsOn("webClientFactory")
@RequiredArgsConstructor
public class PaymentClientImpl implements PaymentClient {

    @Qualifier("paymentClient")
    private final WebClient paymentClient;

    private final BaseRestClient baseRestClient;

    @Override
    public Mono<Optional<MyPaymentDTO>> searchPaymentState(String checkSum, String orderCode, String merchantCode) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        if (!DataUtil.isNullOrEmpty(checkSum)) {
            formData.put("check_sum", Collections.singletonList(checkSum));
        }
        if (!DataUtil.isNullOrEmpty(orderCode)) {
            formData.put("order_code", Collections.singletonList(orderCode));
        }
        if (!DataUtil.isNullOrEmpty(merchantCode)) {
            formData.put("merchant_code", Collections.singletonList(merchantCode));
        }
        return baseRestClient
                .postFormData(paymentClient, "/check-transaction", null, formData, MyPaymentDTO.class)
                .map(response -> {
                    return (Optional<MyPaymentDTO>) response;
                });
    }

    @Override
    public Mono<Optional<MyPaymentDTO>> updateOrderStateForPayment(UpdateOrderStatePayRequest request) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        if (!DataUtil.isNullOrEmpty(request.getTransaction_id())) {
            formData.put("transaction_id", Collections.singletonList(request.getTransaction_id()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCheck_sum())) {
            formData.put("merchant_code", Collections.singletonList(request.getMerchant_code()));
        }
        if (!DataUtil.isNullOrEmpty(request.getCheck_sum())) {
            formData.put("check_sum", Collections.singletonList(request.getCheck_sum()));
        }
        if (!DataUtil.isNullOrEmpty(request.getStatus())) {
            formData.put("status", Collections.singletonList(String.valueOf(request.getStatus())));
        }
        if (!DataUtil.isNullOrEmpty(request.getPayment_status())) {
            formData.put("payment_status", Collections.singletonList(String.valueOf(request.getPayment_status())));
        }
        if (!DataUtil.isNullOrEmpty(request.getOrder_code())) {
            formData.put("order_code", Collections.singletonList(request.getOrder_code()));
        }

        return baseRestClient
                .postFormData(paymentClient, "/updateOrderStatus", null, formData, MyPaymentDTO.class)
                .map(response -> response);
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
}
