package com.ezbuy.cartservice.client.impl;

import com.ezbuy.cartmodel.dto.EstimatePriceDTO;
import com.ezbuy.cartservice.client.PaymentClient;
import com.ezbuy.paymentmodel.dto.request.ProductPriceRequest;
import com.ezbuy.core.client.BaseRestClient;
import com.ezbuy.core.constants.CommonErrorCode;
import com.ezbuy.core.exception.BusinessException;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import com.ezbuy.core.util.Translator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("webClientFactory")
public class PaymentClientImpl implements PaymentClient {

    @Qualifier("payment")
    private final WebClient payment;
    private final BaseRestClient baseRestClient;

    @Override
    public Mono<Optional<Long>> estimatePrice(ProductPriceRequest productPriceRequest) {
        return baseRestClient
                .post(payment, "/price/calculate", null, productPriceRequest, DataResponse.class)
                .map(resp -> {
                    Optional<DataResponse> respOptional = (Optional<DataResponse>) resp;
                    if (respOptional.isEmpty()) {
                        return Mono.error(new BusinessException(
                                CommonErrorCode.INTERNAL_SERVER_ERROR, Translator.toLocaleVi("server.error")));
                    }
                    String jsonValue =
                            DataUtil.parseObjectToString(respOptional.get().getData());
                    EstimatePriceDTO totalPrice = DataUtil.parseStringToObject(jsonValue, EstimatePriceDTO.class);
                    if (totalPrice != null) {
                        return Optional.of(DataUtil.safeToLong(totalPrice.getTotalPrice()));
                    }
                    return Optional.of(0L);
                });
    }
}
