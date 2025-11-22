package com.ezbuy.searchservice.service;

import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.searchservice.dto.request.SearchDTORequest;
import reactor.core.publisher.Mono;

public interface SearchService {

    Mono<DataResponse<Object>> search(SearchDTORequest request);
}
