package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.GroupNewsDTO;
import com.ezbuy.settingservice.model.dto.request.SearchGroupNewsRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GroupNewsRepositoryTemplate {
    Flux<GroupNewsDTO> findGroupNews(SearchGroupNewsRequest request);

    Mono<Long> countGroupNews(SearchGroupNewsRequest request);
}
