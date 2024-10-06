package com.ezbuy.paymentservice.client.impl;

import com.ezbuy.paymentservice.client.properties.OrderClientProperties;
import com.viettel.sme.framework.client.BaseRestClient;
import com.viettel.sme.framework.client.BaseSoapClient;
import com.viettel.sme.framework.constants.CommonErrorCode;
import com.viettel.sme.framework.constants.MessageConstant;
import com.viettel.sme.framework.exception.BusinessException;
import com.viettel.sme.framework.model.response.DataResponse;
import com.viettel.sme.framework.utils.DataUtil;
import com.viettel.sme.framework.utils.Translator;
import com.viettel.sme.ordermodel.dto.request.UpdateLogsRequest;
import com.viettel.sme.ordermodel.dto.request.UpdateOrderStateForOrderRequest;
import com.ezbuy.paymentmodel.constants.ClientUris;
import com.ezbuy.paymentservice.client.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.viettel.sme.framework.constants.CommonErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
@Service
@DependsOn("webClientFactory")
public class OrderClientImpl implements OrderClient {
    private final BaseSoapClient baseSoapClient;
    private final BaseRestClient baseRestClient;
    private final WebClient orderClient;
    private final OrderClientProperties orderProperties;

    public OrderClientImpl(BaseRestClient baseRestClient,
                           @Qualifier("orderClient") WebClient orderClient,
                           BaseSoapClient soapClient,
                           OrderClientProperties orderProperties) {
        this.baseRestClient = baseRestClient;
        this.orderClient = orderClient;
        this.baseSoapClient = soapClient;
        this.orderProperties = orderProperties;
    }

    @Override
    public Mono<DataResponse> updateStatusOrder(String orderCode, Integer orderState) {
        UpdateOrderStateForOrderRequest updateOrderStateForOrderRequest = new UpdateOrderStateForOrderRequest();
        updateOrderStateForOrderRequest.setOrderCode(orderCode);
        updateOrderStateForOrderRequest.setPaymentStatus(orderState);
        return baseRestClient.post(orderClient, ClientUris.Order.UPDATE_PAYMENT_RESULT, null, updateOrderStateForOrderRequest, DataResponse.class)
                .map(response ->{
                        log.info("CM response ", response);
                        return new DataResponse<>(MessageConstant.SUCCESS, null);})
                .onErrorResume(throwable -> {
                    log.error("call api updateOrderState error: {}", throwable);
                    return Mono.just(new DataResponse<>(MessageConstant.SUCCESS, null));
                });
    }
}
