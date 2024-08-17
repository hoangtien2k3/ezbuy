package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.model.ContentDisplay;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContentDisplayRepositoryTemplate {
    Mono<List<ContentDisplayDTO>> getAllByPageId(String pageId);

    Flux<ContentDisplayDTO> getOriginComponent(ComponentPageRequest request);

    Mono<Long> countComponents(ComponentPageRequest request);

    Mono<ContentDisplayDTO> getContentWithParentId(String id);

    Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name);

    Flux<ContentDisplay> getOldContents(String pageId);
}
