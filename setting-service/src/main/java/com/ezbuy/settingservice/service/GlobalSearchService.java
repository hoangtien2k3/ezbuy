package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.request.GlobalSearchSyncRequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface GlobalSearchService {

    Mono<DataResponse> syncService(GlobalSearchSyncRequest request);

    Mono<DataResponse> syncNews(GlobalSearchSyncRequest request);
}
