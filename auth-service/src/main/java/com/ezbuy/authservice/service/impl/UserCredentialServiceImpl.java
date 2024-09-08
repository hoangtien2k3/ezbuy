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
package com.ezbuy.authservice.service.impl;

import com.ezbuy.authmodel.model.UserCredential;
import com.ezbuy.authservice.repository.UserCredentialRepository;
import com.ezbuy.authservice.service.UserCredentialService;
import io.hoangtien2k3.commons.constants.CommonErrorCode;
import io.hoangtien2k3.commons.exception.BusinessException;
import io.hoangtien2k3.commons.utils.SecurityUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public Mono<Optional<UserCredential>> getByUserId(String userId) {
        return userCredentialRepository
                .findByUserId(userId)
                .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
    }

    @Override
    public Mono<Optional<UserCredential>> getCurrentUserCredential() {
        return SecurityUtils.getCurrentUser()
                .flatMap(tokenUser -> {
                    String username = tokenUser.getUsername();
                    return userCredentialRepository
                            .findByUsername(username)
                            .flatMap(userCredential -> Mono.just(Optional.ofNullable(userCredential)))
                            .switchIfEmpty(Mono.error(
                                    new BusinessException(CommonErrorCode.NOT_FOUND, "query.user.not.found")));
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ACCESS_DENIED, "token.error")));
    }
}
