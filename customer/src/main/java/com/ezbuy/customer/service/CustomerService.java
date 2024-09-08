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
package com.ezbuy.customer.service;

import com.ezbuy.customer.model.dto.CustomerDTO;
import com.ezbuy.customer.model.dto.request.AuthenticationRequest;
import com.ezbuy.customer.model.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.model.dto.response.AuthenticationResponse;
import io.hoangtien2k3.commons.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface CustomerService {
    /**
     * Register a new customer
     *
     * @param customerRequest
     * @return DataResponse<CustomerDTO>
     * @author hoangtien2k3
     */
    Mono<DataResponse<CustomerDTO>> createCustomer(CreateCustomerRequest customerRequest);

    /**
     * Login a customer
     *
     * @param authenticationRequest
     * @return DataResponse<AuthenticationResponse>
     * @author hoangtien2k3
     */
    Mono<DataResponse<AuthenticationResponse>> authenticateRequest(AuthenticationRequest authenticationRequest);

    // Mono<DataResponse<TokenDTO>> loginCustomer(CustomerLoginRequest
    // customerLoginRequest);

    // Mono<DataResponse<CustomerResponse>> getCustomerById(Integer customerId);
    //
    // Mono<DataResponse<CustomerResponse>> updateCustomer(Integer customerId,
    // CreateCustomerRequest
    // customerRequest);
    //
    // Mono<DataResponse<CustomerResponse>> deleteCustomer(Integer customerId);
    //
    // Mono<DataResponse<CustomerResponse>> getCustomers();
}
