package com.ezbuy.triggerservice.client.impl;

import com.ezbuy.productmodel.dto.request.CreateSummaryRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.productmodel.dto.request.UnlockVoucherRequest;
import com.ezbuy.triggerservice.client.ProductClient;
import com.reactify.client.BaseRestClient;
import com.reactify.model.response.DataResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@DependsOn("webClientFactory")
public class ProductClientImpl implements ProductClient {

    @Qualifier("productClient")
    private final WebClient productClient;

    private final BaseRestClient restClient;

    @Value("${expired.minutes}")
    public Integer expiredMinutes;

    @Override
    public Mono<DataResponse> syncFilter() {
        return restClient
                .post(productClient, "/v1/sync-filter-template", null, null, DataResponse.class)
                .map(DataResponse::success);
    }

    @Override
    public Mono<DataResponse> syncSubscriber() {
        return restClient
                .post(productClient, "/v1/sync-subscriber", null, null, DataResponse.class)
                .map(DataResponse::success);
    }

    @Override
    public Mono<DataResponse> syncDailyReport() {
        CreateSummaryRequest request = new CreateSummaryRequest();
        LocalDate previousDate = LocalDate.now().minusDays(1); // daily report of previous day
        request.setDateReport(previousDate);
        return restClient.post(productClient, "/v1/create-summary-report", null, request, DataResponse.class);
    }

    @Override
    public Mono<DataResponse> unlockVoucher() {
        UnlockVoucherRequest request = new UnlockVoucherRequest();
        request.setExpiredMinutes(expiredMinutes);
        return restClient.post(productClient, "/v1/voucher/unlock", null, request, DataResponse.class);
    }

    @Override
    public Mono<DataResponse> unlockVoucherTransaction() {
        UnlockVoucherRequest request = new UnlockVoucherRequest();
        request.setExpiredMinutes(expiredMinutes);
        return restClient.post(productClient, "/v1/voucher-transaction/unlock", null, request, DataResponse.class);
    }

    @Override
    public Mono<DataResponse> insertVoucher() {
        return restClient
                .post(productClient, "/v1/voucher/insert-voucher", null, null, DataResponse.class)
                .map(DataResponse::success);
    }
}
