package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.ContentDisplayDTO;
import com.ezbuy.settingservice.model.dto.request.ComponentPageRequest;
import com.ezbuy.settingservice.model.dto.response.SearchingComponentResponse;
import com.ezbuy.settingservice.service.ContentDisplayService;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.DataUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.ContentDisplay.PREFIX)
public class DisplayContentController {

    private final ContentDisplayService contentDisplayService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse<SearchingComponentResponse>>> searchComponents(
            @ModelAttribute ComponentPageRequest request) {
        return this.contentDisplayService
                .searchOriginComponents(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentDisplay.ORIGIN_DETAILS)
    public Mono<ResponseEntity<DataResponse<List<ContentDisplayDTO>>>> getOriginWithDetails(
            @RequestParam(value = "name", required = false) String name) {
        return this.contentDisplayService
                .getOriginComponentDetails(name)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentDisplay.DETAILS)
    public Mono<ResponseEntity<DataResponse<ContentDisplayDTO>>> getDetails(@PathVariable(name = "id") String id) {
        return this.contentDisplayService.getDetails(id).map(rs -> {
            if (DataUtil.isNullOrEmpty(rs.getId())) {
                rs = null;
            }
            return ResponseEntity.ok(new DataResponse<>("success", rs));
        });
    }
}
