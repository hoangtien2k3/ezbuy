package com.ezbuy.productservice.controller;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productmodel.model.ServiceGroup;
import com.ezbuy.productmodel.request.CreateServiceGroupRequest;
import com.ezbuy.productmodel.request.SearchServiceGroupRequest;
import com.ezbuy.productmodel.response.SearchServiceGroupResponse;
import com.ezbuy.productservice.service.ServiceGroupService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(UrlPaths.ServiceGroup.PREFIX)
@RequiredArgsConstructor
public class ServiceGroupController {
    private final ServiceGroupService serviceGroupService;

    @PostMapping
    public Mono<DataResponse<ServiceGroup>> createServiceGroup(@RequestBody CreateServiceGroupRequest request) {
        return serviceGroupService.createServiceGroup(request);
    }

    @PutMapping(value = UrlPaths.ServiceGroup.UPDATE)
    public Mono<DataResponse<ServiceGroup>> editServiceGroup(
            @PathVariable String id, @Valid @RequestBody CreateServiceGroupRequest request) {
        return serviceGroupService.editServiceGroup(id, request);
    }

    @GetMapping
    public Mono<SearchServiceGroupResponse> findServiceGroup(@ModelAttribute SearchServiceGroupRequest request) {
        return serviceGroupService.findServiceGroup(request);
    }

    @GetMapping(UrlPaths.ServiceGroup.ALL)
    public Mono<DataResponse<List<ServiceGroup>>> getAllServiceGroupActive() {
        return this.serviceGroupService
                .getAllServiceGroupActive()
                .map(rs -> new DataResponse(Translator.toLocaleVi("success"), rs));
    }
}
