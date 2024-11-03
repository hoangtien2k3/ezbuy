package com.ezbuy.productservice.controller;

import static com.ezbuy.productmodel.constants.Constants.Message.SUCCESS;

import com.ezbuy.productmodel.constants.UrlPaths;
import com.ezbuy.productservice.service.TelecomServiceService;
import com.reactify.model.response.DataResponse;
import com.reactify.util.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(UrlPaths.ACTIVATE_TELECOM.PREFIX)
@RequiredArgsConstructor
public class TelecomServiceController {

    private final TelecomServiceService telecomServiceService;

    @GetMapping
    public Mono<DataResponse<?>> getFilterTemplate(@RequestParam(name = "orgId") String orgId) {
        return telecomServiceService
                .getTelecomServices()
                .collectList()
                .map(result -> new DataResponse<>(Translator.toLocaleVi(SUCCESS), result));
    }
}
