package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.ContentSectionDTO;
import com.ezbuy.settingservice.model.dto.ContentSectionDetailDTO;
import com.ezbuy.settingservice.model.dto.request.SearchContentSectionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentSectionRepositoryTemplate {
    Flux<ContentSectionDTO> queryList(SearchContentSectionRequest request);

    Mono<Long> count(SearchContentSectionRequest request);

    Flux<ContentSectionDetailDTO> getDetailContentSection(String id);
}
