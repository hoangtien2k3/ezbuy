/*
 * Copyright 2024 the original author Hoàng Anh Tiến.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ezbuy.authservice.controller;

import com.ezbuy.authmodel.constants.UrlPaths;
import com.ezbuy.authservice.service.UserCredentialService;
import io.hoangtien2k3.commons.model.response.DataResponse;
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
