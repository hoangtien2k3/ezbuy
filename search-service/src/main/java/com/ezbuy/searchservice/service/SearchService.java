package com.ezbuy.searchservice.service;

import com.ezbuy.searchmodel.dto.request.SearchDTORequest;
import com.reactify.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface SearchService {

    Mono<DataResponse<Object>> search(SearchDTORequest request);
}
