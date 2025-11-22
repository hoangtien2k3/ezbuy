package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.ContentSectionDetailDTO;
import com.ezbuy.settingservice.model.dto.TreeDataDTO;
import com.ezbuy.settingservice.model.dto.request.GetContentSectionRequest;
import com.ezbuy.settingservice.model.entity.ContentSection;
import com.ezbuy.settingservice.model.dto.request.ContentSectionRequest;
import com.ezbuy.settingservice.model.dto.request.GetBySectionRequest;
import com.ezbuy.settingservice.model.dto.request.GetByServiceRequest;
import com.ezbuy.settingservice.model.dto.request.SearchContentSectionRequest;
import com.ezbuy.settingservice.model.dto.request.v2.GetByServiceRequestV2;
import com.ezbuy.settingservice.model.dto.response.SearchContentSectionResponse;
import com.ezbuy.settingservice.service.ContentSectionService;
import com.ezbuy.core.model.response.DataResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.ContentSection.PREFIX)
public class ContentSectionController {
    private final ContentSectionService contentSectionService;

    @GetMapping(UrlPaths.ContentSection.SEARCH)
    public Mono<ResponseEntity<DataResponse<SearchContentSectionResponse>>> search(
            @RequestBody SearchContentSectionRequest request) {
        return contentSectionService.search(request).map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.ContentSection.GET_ALL_SA)
    public Mono<ResponseEntity<DataResponse<List<ContentSection>>>> getAll() {
        return contentSectionService.getAllCS().map(result -> ResponseEntity.ok(new DataResponse<>("success", result)));
    }

    @PostMapping()
    public Mono<DataResponse<DataResponse<ContentSection>>> create(@Valid @RequestBody ContentSectionRequest request) {
        return contentSectionService.createCS(request).map(result -> new DataResponse<>("success", result));
    }

    @PutMapping()
    public Mono<DataResponse<ContentSection>> update(@Valid @RequestBody ContentSectionRequest request) {
        return contentSectionService.updateCS(request);
    }

    @GetMapping(UrlPaths.ContentSection.ALL_ACTIVE)
    public Mono<DataResponse<List<ContentSection>>> getAllActiveSA() {
        return contentSectionService.getAllActiveCS().map(result -> new DataResponse<>("success", result));
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

    @GetMapping
    public Mono<ResponseEntity<DataResponse<List<TreeDataDTO>>>> getAllByTypeAndRefIdAndRefType(
            @RequestBody GetContentSectionRequest request) {
        return contentSectionService
                .getAllByTypeAndRefIdAndRefType(request)
                .flatMap(contentSection -> Mono.just(ResponseEntity.ok(new DataResponse<>("success", contentSection))));
    }

    @GetMapping(UrlPaths.ContentSection.DETAILS)
    public Mono<DataResponse<ContentSectionDetailDTO>> getAllActiveSA(@PathVariable("id") String id) {
        return contentSectionService.getContentSectionDetail(id).map(result -> new DataResponse<>("success", result));
    }

    @DeleteMapping(UrlPaths.ContentSection.DELETE)
    public Mono<DataResponse<Boolean>> delete(@PathVariable String id) {
        return contentSectionService.delete(id).map(result -> new DataResponse<>("success", result));
    }
}
