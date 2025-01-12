package com.ezbuy.productservice.client;

import com.ezbuy.productmodel.dto.request.CallApiSyncProductRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface SyncClient {
    Mono<DataResponse> callApiSyncProduct(CallApiSyncProductRequest request);

    /**
     * Ham thuc hien lay sync transId de thuc hien dong bo
     *
     * @param userName:
     *            userName nguoi thuc hien tac dong len ban ghi
     * @return
     */
    Mono<String> getProductSyncTransId(String userName);
}
