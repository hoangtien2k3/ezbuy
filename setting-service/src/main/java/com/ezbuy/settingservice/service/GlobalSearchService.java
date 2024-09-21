package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.request.GlobalSearchSyncRequest;
import io.hoangtien2k3.commons.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface GlobalSearchService {

    Mono<DataResponse> syncService(GlobalSearchSyncRequest request);

    Mono<DataResponse> syncNews(GlobalSearchSyncRequest request);
}
