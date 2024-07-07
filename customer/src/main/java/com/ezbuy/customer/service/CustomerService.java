package com.ezbuy.customer.service;

import com.ezbuy.customer.dto.response.CustomerResponse;
import com.ezbuy.customer.model.Customer;
import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;
import com.ezbuy.customer.dto.request.CreateCustomerRequest;

public interface CustomerService {
    Mono<DataResponse<Customer>> createCustomer(CreateCustomerRequest customerRequest);
    Mono<DataResponse<CustomerResponse>> getCustomerById(Integer customerId);
    Mono<DataResponse<CustomerResponse>> updateCustomer(Integer customerId, CreateCustomerRequest customerRequest);
    Mono<DataResponse<CustomerResponse>> deleteCustomer(Integer customerId);
    Mono<DataResponse<CustomerResponse>> getCustomers();
}
