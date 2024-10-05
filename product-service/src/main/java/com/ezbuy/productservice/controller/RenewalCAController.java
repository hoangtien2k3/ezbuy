package com.ezbuy.productservice.controller;

import com.ezbuy.productservice.service.RenewalCAService;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.request.ProductSpecificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(DEFAULT_V1_PREFIX)
@RequiredArgsConstructor
public class RenewalCAController {

    private final RenewalCAService renewalCAService;

    /**
     * Bo sung alias cho dich vu vao response
     * @param organizationId
     * @param time
     * @return
     */
    @GetMapping(value = STATISTIC_SUBSCRIBER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse> getStatisticSubscriber(String organizationId, Integer time) {
        return renewalCAService.getStatisticSubscriber(organizationId, time);
    }

    @GetMapping(value = GET_LIST_SUBSCRIBER)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse> getListSubscriber(Long telecomServiceId, String telecomServiceAlias, String organizationId) {
        return renewalCAService.getListSubscriber(telecomServiceId, telecomServiceAlias, organizationId);
    }

    @PostMapping(value = GET_PRODUCT_SPECIFICATION)
    @PreAuthorize("hasAnyAuthority('user')")
    public Mono<DataResponse> getProductSpecification(@RequestBody ProductSpecificationRequest request) {
        return renewalCAService.getProductSpecification(request);
    }

    @PostMapping(value = SYNC_SUBSCRIBER)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse> syncListSubscriber() {
        return renewalCAService.syncListSubscriber();
    }
}
