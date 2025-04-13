package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authmodel.dto.request.UserOtpRequest;
import com.ezbuy.authservice.service.UserOtpService;
import com.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.UserOtp.PREFIX)
public class UserOtpController {

    private final UserOtpService userOtpService;

    @GetMapping
    public Mono<ResponseEntity<DataResponse<?>>> search(UserOtpRequest request) {
        return userOtpService
                .search(request)
                .flatMap(rs -> Mono.just(ResponseEntity.ok(new DataResponse<>("common.success", rs))));
    }
}
