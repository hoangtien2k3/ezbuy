package com.ezbuy.settingservice.controller;

import com.ezbuy.settingservice.constants.UrlPaths;
import com.ezbuy.settingservice.model.dto.TelecomDTO;
import com.ezbuy.settingservice.model.dto.TelecomServiceConfigDTO;
import com.ezbuy.settingservice.model.dto.request.GetServiceConfigRequest;
import com.ezbuy.settingservice.model.dto.response.ClientTelecom;
import com.ezbuy.settingservice.model.dto.response.PageResponse;
import com.ezbuy.settingservice.model.dto.response.TelecomClient;
import com.ezbuy.settingservice.model.dto.response.TelecomPagingResponse;
import com.ezbuy.settingservice.model.dto.response.TelecomResponse;
import com.ezbuy.settingservice.model.entity.Telecom;
import com.ezbuy.settingservice.model.dto.request.PageTelecomRequest;
import com.ezbuy.settingservice.model.dto.request.StatusLockingRequest;
import com.ezbuy.settingservice.model.dto.request.TelecomSearchingRequest;
import com.ezbuy.settingservice.service.TelecomService;
import com.ezbuy.core.constants.MessageConstant;
import com.ezbuy.core.model.response.DataResponse;
import com.ezbuy.core.util.Translator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = UrlPaths.Telecom.PREFIX)
public class TelecomController {
    private final TelecomService telecomService;

    @GetMapping
    public Mono<DataResponse<List<TelecomDTO>>> getTelecomService(
            @RequestParam(required = false) List<String> ids,
            @RequestParam(required = false) List<String> aliases,
            @RequestParam(required = false) List<String> origins) {
        return telecomService.getTelecomService(ids, aliases, origins);
    }

    @GetMapping(UrlPaths.Telecom.ORIGIN_FILTER_V2)
    public Mono<DataResponse<List<Telecom>>> getTelecomByOriginV2(@RequestParam String serviceAlias) {
        return telecomService.getByServiceAlias(serviceAlias);
    }

    @GetMapping(UrlPaths.Telecom.ORIGIN_FILTER)
    public Mono<DataResponse<List<Telecom>>> getTelecomByOrigin(
            @RequestParam String originId, @RequestParam(required = false) String serviceAlias) {
        return telecomService.getByOriginId(originId, serviceAlias);
    }

    @GetMapping(UrlPaths.Telecom.LIST)
    public Mono<ResponseEntity<DataResponse<TelecomPagingResponse>>> getTelecomService(
            @ModelAttribute TelecomSearchingRequest request) {
        return telecomService
                .searchTelecomService(request)
                .map(rs -> ResponseEntity.ok(new DataResponse<>("success", rs)));
    }

    @GetMapping(UrlPaths.Telecom.NON_INIT_FILTER)
    public Mono<DataResponse<List<Telecom>>> getNonFilterTelecom() {
        return telecomService.getNonFilterTelecom().map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(UrlPaths.Telecom.LOCK_UNLOCK)
    public Mono<ResponseEntity<DataResponse>> updateStatus(@ModelAttribute StatusLockingRequest params) {
        return telecomService.updateStatus(params).map(rs -> ResponseEntity.ok(new DataResponse<>("success", null)));
    }

    @GetMapping(UrlPaths.Telecom.INIT_FILTER)
    public Mono<DataResponse> initFilter(@RequestParam("originId") String originId) {
        return telecomService.initFilter(originId);
    }

    @GetMapping(UrlPaths.Telecom.INIT_FILTER_V2)
    public Mono<DataResponse> initFilterV2(
            @RequestParam(value = "serviceAlias", required = false) String serviceAlias) {
        return telecomService.initFilterV2(serviceAlias);
    }

    @GetMapping(value = UrlPaths.Telecom.LIST_REQUEST)
    public Mono<DataResponse<PageResponse>> getPageTelecomService(PageTelecomRequest request) {
        return telecomService.getPageTelecomService(request);
    }

    @GetMapping(UrlPaths.Telecom.SERVICE_TYPES)
    public Mono<DataResponse<List<String>>> getServiceTypes() {
        return telecomService.getServiceTypes();
    }

    @GetMapping(UrlPaths.Telecom.GET_ALL_ACTIVE)
    public Mono<DataResponse<List<TelecomResponse>>> getAllTelecomServiceActive() {
        return telecomService.getAllTelecomServiceActive().map(rs -> new DataResponse<>("success", rs));
    }

    @GetMapping(value = UrlPaths.Telecom.GET_ADMIN_ROLE)
    public Mono<DataResponse<TelecomClient>> getAdminRole(@RequestParam String originId) {
        return telecomService
                .getAdminRoleOfService(originId)
                .map(rs -> new DataResponse<>(Translator.toLocale(MessageConstant.SUCCESS), rs));
    }

    @GetMapping(value = UrlPaths.Telecom.GET_ADMIN_ROLE_V2)
    public Mono<DataResponse<TelecomClient>> getAdminRoleV2(@RequestParam String serviceAlias) {
        return telecomService
                .getAdminRoleOfServiceByServiceAlias(serviceAlias)
                .map(rs -> new DataResponse<>(Translator.toLocale(MessageConstant.SUCCESS), rs));
    }

    @GetMapping(value = UrlPaths.Telecom.GET_SERVICE_CONFIG)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<List<TelecomServiceConfigDTO>>> getServiceConfig(
            @RequestParam(name = "telecomIds", required = false) List<String> telecomServiceIds,
            @RequestParam(name = "originalIds", required = false) List<String> originalIds,
            @RequestParam(name = "syncType") String syncType) {
        return telecomService.getTelecomServiceConfig(telecomServiceIds, originalIds, syncType);
    }

    @PostMapping(value = UrlPaths.Telecom.GET_SERVICE_CONFIG_V2)
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse<List<TelecomServiceConfigDTO>>> getServiceConfigV2(
            @RequestBody GetServiceConfigRequest request) {
        return telecomService.getTelecomServiceConfigV2(request);
    }

    @GetMapping(value = UrlPaths.Telecom.GET_ALL_TELECOM_SERVICE)
    public Mono<DataResponse<List<Telecom>>> getAllTelecomServiceIdAndCode() {
        return telecomService.getAllTelecomServiceIdAndCode();
    }

    @GetMapping(value = UrlPaths.Telecom.GET_TELECOM_BY_LIST_ORIGIN_ID)
    public Mono<DataResponse<List<Telecom>>> getTelecomByLstOriginId(@RequestParam List<String> lstOriginId) {
        return telecomService.getTelecomByLstOriginId(lstOriginId);
    }

    @GetMapping(UrlPaths.Telecom.GET_AlIAS_BY_CLIENT_CODE)
    public Mono<DataResponse<ClientTelecom>> getAliasByClientCode(@RequestParam String clientCode) {
        return telecomService
                .getAliasByClientCode(clientCode)
                .map(rs -> new DataResponse<>(Translator.toLocale(MessageConstant.SUCCESS), rs));
    }
}
