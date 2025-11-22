package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.request.QueryNewsRequest;
import com.ezbuy.settingservice.model.dto.response.QueryNewsResponse;
import reactor.core.publisher.Mono;

public interface NewsContentRepositoryTemplate {

    Mono<QueryNewsResponse> queryNewsContent(QueryNewsRequest request);
}
