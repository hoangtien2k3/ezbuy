package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.model.Page;
import com.ezbuy.settingmodel.request.SearchingPageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PageRepositoryTemplate {
    Flux<Page> queryPages(SearchingPageRequest request);

    Mono<Long> countPages(SearchingPageRequest request);
}
