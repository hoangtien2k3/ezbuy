package com.ezbuy.productservice.controller;

import com.ezbuy.productservice.service.TelecomService;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.productmodel.constants.UrlPaths;
import com.ezbuy.sme.settingmodel.model.Telecom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.ezbuy.sme.productmodel.constants.UrlPaths.DEFAULT_V1_PREFIX;
import static com.ezbuy.sme.productmodel.constants.UrlPaths.GET_TELECOM_SERVICE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(UrlPaths.Service.PREFIX)
@CrossOrigin
public class TelecomController {
    private final TelecomService telecomService;

    @GetMapping()
    public Mono<DataResponse<Telecom>> getTelecomByOrigin(@RequestParam(name = "originId") String originId) {
        return this.telecomService.getByOriginId(originId);
    }

    @GetMapping(value = UrlPaths.Service.ALL)
    public Mono<DataResponse<List<Telecom>>> getAllActive() {
        return this.telecomService.getAllActive();
    }

    @GetMapping(value = UrlPaths.Service.GET_BY_ALIAS)
    public Mono<DataResponse<Telecom>> getTelecomByAlias(@RequestParam(name = "telecomServiceAlias") String telecomServiceAlias) {
        return this.telecomService.getByAlias(telecomServiceAlias);
    }

    @PostMapping(value = UrlPaths.Service.GET_BY_LST_ALIAS)
    public Mono<DataResponse<List<Telecom>>> getTelecomByLstAlias(@RequestBody List<String> lstTelecomServiceAlias) {
        return this.telecomService.getByLstAlias(lstTelecomServiceAlias);
    }
}
