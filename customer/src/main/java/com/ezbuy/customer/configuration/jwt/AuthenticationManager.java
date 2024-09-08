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
package com.ezbuy.customer.configuration.jwt;

import com.ezbuy.customer.service.impl.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        return Mono.just(authToken)
                .filter(jwtService::isTokenValid)
                .flatMap(token -> reactiveUserDetailsService
                        .findByUsername(jwtService.extractUsername(token))
                        .map(userDetails -> new JwtToken(token, userDetails))
                        .cast(Authentication.class))
                .map(auth -> ((JwtToken) auth).withAuthenticated(true))
                .switchIfEmpty(Mono.error(new JwtAuthenticationException("Invalid token.")));
    }
}
