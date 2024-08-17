package com.ezbuy.settingservice.service;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.dto.request.GlobalSearchSyncRequest;
import reactor.core.publisher.Mono;

public interface GlobalSearchService {

    Mono<DataResponse> syncService(GlobalSearchSyncRequest request);
    Mono<DataResponse> syncNews(GlobalSearchSyncRequest request);
}
