package com.ezbuy.settingservice.controller;

import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.settingmodel.dto.ContentSectionDetailDTO;
import com.ezbuy.settingmodel.dto.TreeDataDTO;
import com.ezbuy.settingmodel.request.ContentSectionRequest;
import com.ezbuy.settingmodel.request.GetBySectionRequest;
import com.ezbuy.settingmodel.request.GetByServiceRequest;
import com.ezbuy.settingmodel.request.SearchContentSectionRequest;
import com.ezbuy.settingmodel.constants.UrlPaths;
import com.ezbuy.settingmodel.model.ContentSection;
import com.ezbuy.settingmodel.request.v2.GetByServiceRequestV2;
import com.ezbuy.settingmodel.response.SearchContentSectionResponse;
import com.ezbuy.settingservice.service.ContentSectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import com.ezbuy.settingmodel.dto.request.GetContentSectionRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.ContentSection.PREFIX)
public class ContentSectionController {
    private final ContentSectionService contentSectionService;

    @GetMapping(UrlPaths.ContentSection.SEARCH)
    public Mono<ResponseEntity<DataResponse<SearchContentSectionResponse>>> search(SearchContentSectionRequest request) {
        return contentSectionService.search(request).map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentSection.GET_ALL_SA)
    public Mono<ResponseEntity<DataResponse<List<ContentSection>>>> getAll() {
        return contentSectionService.getAllCS()
                .map(result -> ResponseEntity.ok(new DataResponse<>("success", result)));
    }

    @PostMapping()
    public Mono<DataResponse<DataResponse<ContentSection>>> create(@Valid @RequestBody ContentSectionRequest request) {
        return contentSectionService.createCS(request)
                .map(result -> new DataResponse<>("success", result));
    }

    @PutMapping()
    public Mono<DataResponse<ContentSection>> update(@Valid @RequestBody ContentSectionRequest request) {
        return contentSectionService.updateCS(request);
    }

    @GetMapping(UrlPaths.ContentSection.ALL_ACTIVE)
    public Mono<DataResponse<List<ContentSection>>> getAllActiveSA() {
        return contentSectionService.getAllActiveCS()
                .map(result -> new DataResponse<>("success", result));
    }

    @PostMapping(UrlPaths.ContentSection.LIST_BY_SERVICE)
    public Mono<DataResponse<List<ContentSection>>> getCSByServiceId(@RequestBody GetByServiceRequest request) {
        return contentSectionService.getCSByServiceId(request.getLstServiceId());
    }

    @PostMapping(UrlPaths.ContentSection.LIST_BY_SERVICE_V2)
    public Mono<DataResponse<List<ContentSection>>> getCSByServiceIdV2(@RequestBody GetByServiceRequestV2 request) {
        return contentSectionService.getCSByServiceIdV2(request.getLstAlias());
    }

    @PostMapping(UrlPaths.ContentSection.LiST_BY_SECTION)
    public Mono<DataResponse<List<ContentSection>>> getCSBySectionId(@RequestBody GetBySectionRequest request) {
        return contentSectionService.getCSBySectionId(request.getLstSectionId());
    }

    @GetMapping()
    public Mono<ResponseEntity<DataResponse<List<TreeDataDTO>>>> getAllByTypeAndRefIdAndRefType(GetContentSectionRequest request) {
        return contentSectionService.getAllByTypeAndRefIdAndRefType(request)
                .flatMap(contentSection ->
                        Mono.just(ResponseEntity.ok(new DataResponse<>("success", contentSection))));
    }

    @GetMapping(UrlPaths.ContentSection.DETAILS)
    public Mono<DataResponse<ContentSectionDetailDTO>> getAllActiveSA(@PathVariable("id") String id) {
        return contentSectionService.getContentSectionDetail(id)
                .map(result -> new DataResponse<>("success", result));
    }

    @DeleteMapping(UrlPaths.ContentSection.DELETE)
    public Mono<DataResponse<Boolean>> delete(@PathVariable String id) {
        return contentSectionService.delete(id)
                .map(result -> new DataResponse<>("success", result));
    }
}
