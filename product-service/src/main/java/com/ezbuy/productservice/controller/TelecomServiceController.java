package com.ezbuy.productservice.controller;

import com.ezbuy.productservice.service.TelecomServiceService;
import com.ezbuy.sme.framework.model.response.DataResponse;
import com.ezbuy.sme.framework.utils.Translator;
import com.ezbuy.sme.productmodel.constants.UrlPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.ezbuy.sme.framework.constants.MessageConstant.SUCCESS;

@RestController
@RequestMapping(UrlPaths.ACTIVATE_TELECOM.PREFIX)
@RequiredArgsConstructor
public class TelecomServiceController {

    private final TelecomServiceService telecomServiceService;

    @GetMapping
    public Mono<DataResponse<?>> getFilterTemplate(@RequestParam(name = "orgId") String orgId) {
        return telecomServiceService.getTelecomServices()
                .collectList()
                .map(result -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), result));
    }

}
