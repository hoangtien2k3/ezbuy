package com.ezbuy.searchservice.controller;

import com.ezbuy.searchservice.dto.request.SearchDTORequest;
import com.ezbuy.searchservice.service.SearchService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/global-search")
public class SearchController {

    private final SearchService searchService;

    @PostMapping
    public Mono<DataResponse<Object>> search(@RequestBody SearchDTORequest searchDTORequest) {
        return searchService.search(searchDTORequest);
    }
}
