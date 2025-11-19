package com.ezbuy.searchservice.controller;

import com.ezbuy.searchmodel.constants.UrlPaths;
import com.ezbuy.searchmodel.dto.request.SearchDTORequest;
import com.ezbuy.searchservice.service.SearchService;
import com.ezbuy.core.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.GlobalSearch.PREFIX)
public class SearchController {

    private final SearchService searchService;

    @PostMapping()
    public Mono<DataResponse<Object>> search(@RequestBody SearchDTORequest searchDTORequest) {
        return searchService.search(searchDTORequest);
    }
}
