package com.ezbuy.productservice.controller;

import static com.ezbuy.productmodel.constants.UrlPaths.DEFAULT_V1_PREFIX;

import com.ezbuy.productservice.service.InfoSerService;
import io.hoangtien2k3.reactify.Translator;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(DEFAULT_V1_PREFIX)
@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET, RequestMethod.POST},
        allowedHeaders = {"*"},
        maxAge = 3600)
public class InfoServiceController {

    private final InfoSerService infoSerService;

    @GetMapping("/active-telecom-services")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse> getActiveTelecomService(@RequestParam(name = "idNos") List<String> idNos) {
        return infoSerService
                .getActiveTelecomService(idNos)
                .map(rs -> new DataResponse<>(Translator.toLocaleVi("success"), rs));
    }

    @GetMapping("/active-telecom-service-id")
    @PreAuthorize("hasAnyAuthority('system')")
    public Mono<DataResponse> getActiveTelecomServiceIdByIdNo(@RequestParam(name = "idNo") String idNo) {
        return infoSerService
                .getActiveTelecomServiceIdByOrgId(idNo)
                .map(rs -> new DataResponse<>(Translator.toLocaleVi("success"), rs));
    }

    @GetMapping("/active-telecom-service-alias")
    public Mono<DataResponse> getActiveTelecomServiceAliasByIdNo(@RequestParam(name = "idNo") String idNo) {
        return infoSerService
                .getActiveTelecomServiceAlilasByIdNo(idNo)
                .map(rs -> new DataResponse<>(Translator.toLocaleVi("success"), rs));
    }
}
