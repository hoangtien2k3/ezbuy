package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.request.GlobalSearchSyncRequest;
import com.ezbuy.core.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface GlobalSearchService {

    Mono<DataResponse> syncService(GlobalSearchSyncRequest request);

    Mono<DataResponse> syncNews(GlobalSearchSyncRequest request);
}
