package com.ezbuy.productservice.client;

import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.request.CallApiSyncProductRequest;
import reactor.core.publisher.Mono;

public interface SyncClient {
    Mono<DataResponse> callApiSyncProduct(CallApiSyncProductRequest request);

    /**
     * Ham thuc hien lay sync transId de thuc hien dong bo
     * @param userName: userName nguoi thuc hien tac dong len ban ghi
     * @return
     */
    Mono<String> getProductSyncTransId(String userName);
}
