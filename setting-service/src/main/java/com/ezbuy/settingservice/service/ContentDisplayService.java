package com.ezbuy.settingservice.service;

import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import com.ezbuy.settingmodel.response.SearchingComponentResponse;
import java.util.List;
import reactor.core.publisher.Mono;

public interface ContentDisplayService {

    Mono<SearchingComponentResponse> searchOriginComponents(ComponentPageRequest request);

    Mono<ContentDisplayDTO> getDetails(String id);

    Mono<List<ContentDisplayDTO>> getOriginComponentDetails(String name);
}
