package com.ezbuy.customerservice.domain.customer.service;

import com.ezbuy.customermodel.dto.CustomerDTO;
import com.ezbuy.customermodel.dto.request.CreateCustomerRequest;
import com.ezbuy.framework.model.response.DataResponse;
import reactor.core.publisher.Mono;

public interface CustomerService {
    /**
     * Register a new customer
     *
     * @param customerRequest
     * @return
     * @author hoangtien2k3
     */
    Mono<DataResponse<CustomerDTO>> createCustomer(CreateCustomerRequest customerRequest);

//    Mono<DataResponse<CustomerResponse>> getCustomerById(Integer customerId);
//
//    Mono<DataResponse<CustomerResponse>> updateCustomer(Integer customerId, CreateCustomerRequest customerRequest);
//
//    Mono<DataResponse<CustomerResponse>> deleteCustomer(Integer customerId);
//
//    Mono<DataResponse<CustomerResponse>> getCustomers();
}
