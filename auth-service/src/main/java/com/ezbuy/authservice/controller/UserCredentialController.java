package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authservice.service.UserCredentialService;
import io.hoangtien2k3.reactify.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UrlPaths.UserCredential.PREFIX)
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<DataResponse<Object>>> getByUserId(@PathVariable String userId) {
        return userCredentialService
                .getByUserId(userId)
                .map(result -> ResponseEntity.ok(new DataResponse<>("common.success", result)));
    }

    @GetMapping()
    public Mono<ResponseEntity<DataResponse<Object>>> getCurrentUserCredential() {
        return userCredentialService
                .getCurrentUserCredential()
                .map(result -> ResponseEntity.ok(new DataResponse<>("common.success", result)));
    }
}
