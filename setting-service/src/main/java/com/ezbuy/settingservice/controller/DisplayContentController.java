package com.ezbuy.settingservice.controller;

import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.dto.ContentDisplayDTO;
import com.ezbuy.settingmodel.request.ComponentPageRequest;
import com.ezbuy.settingmodel.response.SearchingComponentResponse;
import com.ezbuy.settingservice.service.ContentDisplayService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.DataUtil;
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

    @GetMapping()
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
