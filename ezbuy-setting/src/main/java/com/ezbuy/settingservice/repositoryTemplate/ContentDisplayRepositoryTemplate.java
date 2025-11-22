package com.ezbuy.settingservice.repositoryTemplate;

import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.entity.ContentDisplay;
import com.ezbuy.settingservice.model.dto.request.ComponentPageRequest;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContentDisplayRepositoryTemplate {
    Mono<List<ContentDisplayDTO>> getAllByPageId(String pageId);

    Flux<ContentDisplayDTO> getOriginComponent(ComponentPageRequest request);

    Mono<Long> countComponents(ComponentPageRequest request);

    Mono<ContentDisplayDTO> getContentWithParentId(String id);

    Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name);

    Flux<ContentDisplay> getOldContents(String pageId);
}
