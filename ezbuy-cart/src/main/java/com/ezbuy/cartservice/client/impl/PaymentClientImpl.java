package com.ezbuy.cartservice.client.impl;

import com.ezbuy.cartservice.domain.dto.EstimatePriceDTO;
import com.ezbuy.cartservice.domain.dto.request.ProductPriceRequest;
import com.ezbuy.cartservice.client.PaymentClient;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.constants.ErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.Translator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@DependsOn("webClientFactory")
public class PaymentClientImpl implements PaymentClient {

    private final WebClient payment;
    private final BaseRestClient baseRestClient;

    public PaymentClientImpl(@Qualifier("paymentServiceClient") WebClient payment,
                             BaseRestClient baseRestClient) {
        this.payment = payment;
        this.baseRestClient = baseRestClient;
    }

    @Override
    public Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest) {
        return baseRestClient
                .post(payment, "/price/calculate", null, productPriceRequest, DataResponse.class)
                .flatMap(resp -> {
                    if (resp.isEmpty()) {
                        return Mono.error(new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("server.error")));
                    }
                    String jsonValue = DataUtil.parseObjectToString(resp.get().getData());
                    EstimatePriceDTO totalPrice = DataUtil.parseStringToObject(jsonValue, EstimatePriceDTO.class);
                    if (totalPrice != null) {
                        return Mono.just(Optional.of(DataUtil.safeToLong(totalPrice.getTotalPrice())));
                    }
                    return Mono.just(Optional.of(0L));
                });
    }
}
