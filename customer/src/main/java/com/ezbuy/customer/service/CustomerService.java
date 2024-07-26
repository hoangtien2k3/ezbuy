package com.ezbuy.customer.service;

import com.ezbuy.customer.model.dto.CustomerDTO;
import com.ezbuy.customer.model.dto.request.AuthenticationRequest;
import com.ezbuy.customer.model.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.model.dto.response.AuthenticationResponse;
import com.ezbuy.framework.model.response.DataResponse;

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

    //    Mono<DataResponse<TokenDTO>> loginCustomer(CustomerLoginRequest customerLoginRequest);

    //    Mono<DataResponse<CustomerResponse>> getCustomerById(Integer customerId);
    //
    //    Mono<DataResponse<CustomerResponse>> updateCustomer(Integer customerId,
    // CreateCustomerRequest
    // customerRequest);
    //
    //    Mono<DataResponse<CustomerResponse>> deleteCustomer(Integer customerId);
    //
    //    Mono<DataResponse<CustomerResponse>> getCustomers();
}
