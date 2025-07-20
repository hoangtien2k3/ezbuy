package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.request.QueryNewsRequest;
import com.ezbuy.settingmodel.dto.response.QueryNewsResponse;
import reactor.core.publisher.Mono;

public interface NewsContentRepositoryTemplate {

    Mono<QueryNewsResponse> queryNewsContent(QueryNewsRequest request);
}
