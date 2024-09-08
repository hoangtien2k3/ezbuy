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
package com.ezbuy.customer.controller;

import com.ezbuy.customer.model.dto.CustomerDTO;
import com.ezbuy.customer.model.dto.request.AuthenticationRequest;
import com.ezbuy.customer.model.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.model.dto.response.AuthenticationResponse;
import com.ezbuy.customer.service.CustomerService;
import io.hoangtien2k3.commons.model.response.DataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/create")
    public Mono<DataResponse<CustomerDTO>> createCustomer(@RequestBody CreateCustomerRequest customerRequest) {
        return customerService
                .createCustomer(customerRequest)
                .doOnError(throwable -> log.error("Error creating customer: {}", throwable.getMessage()));
    }

    @PostMapping("/auth")
    public Mono<DataResponse<AuthenticationResponse>> authenticateCustomer(@RequestBody AuthenticationRequest auth) {
        return customerService
                .authenticateRequest(auth)
                .doOnError(throwable -> log.error("Error authenticating customer: {}", throwable.getMessage()));
    }

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Hello World");
    }

    @GetMapping("/access")
    public Mono<String> testAccess() {
        return Mono.just("Access successful");
    }
}
