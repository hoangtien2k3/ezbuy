package com.ezbuy.settingservice.service;

import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.dto.request.ComponentPageRequest;
import com.ezbuy.settingservice.model.dto.response.SearchingComponentResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ContentDisplayService {

    Mono<SearchingComponentResponse> searchOriginComponents(ComponentPageRequest request);

    Mono<ContentDisplayDTO> getDetails(String id);

    Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name);
}
