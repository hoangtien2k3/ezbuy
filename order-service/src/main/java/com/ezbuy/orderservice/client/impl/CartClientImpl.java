package com.ezbuy.orderservice.client.impl;

import com.ezbuy.cartmodel.dto.DeleteUserProductCartDTO;
import com.ezbuy.orderservice.client.CartClient;
import io.hoangtien2k3.reactify.client.BaseRestClient;
import io.hoangtien2k3.reactify.constants.MessageConstant;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@DependsOn("webClientFactory")
public class CartClientImpl implements CartClient {

    private final BaseRestClient baseRestClient;

    private final WebClient cartClient;

    public CartClientImpl(BaseRestClient baseRestClient, @Qualifier("cartClient") WebClient cartClient) {
        this.baseRestClient = baseRestClient;
        this.cartClient = cartClient;
    }

    @Override
    public Mono<DataResponse> clearAllCartItem(String userId, List<String> templateIds) {
        DeleteUserProductCartDTO deleteUserProductCartDTO = DeleteUserProductCartDTO.builder()
                .userId(userId)
                .listProductId(templateIds)
                .build();
        return baseRestClient
                .post(cartClient, "/v1/cart-item/delete-list-item", null, deleteUserProductCartDTO, DataResponse.class)
                .map(response -> returnSuccessDataResponse())
                .onErrorResume(throwable -> {
                    log.error("call api clearAllCartItem error: {}", throwable);
                    return Mono.just(returnSuccessDataResponse());
                });
    }

    private DataResponse returnSuccessDataResponse() {
        return new DataResponse<>(MessageConstant.SUCCESS, null);
    }
}
