package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.ContentSectionDTO;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentSectionRepositoryTemplate {
    Flux<ContentSectionDTO> queryList(SearchContentSectionRequest request);
    Mono <Long> count(SearchContentSectionRequest request);
    Flux<ContentSectionDetailDTO> getDetailContentSection(String id);
}
