package com.ezbuy.customer.controller;

import com.ezbuy.customer.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.dto.response.CustomerResponse;
import com.ezbuy.customer.model.Customer;
import com.ezbuy.customer.service.CustomerService;
import com.ezbuy.framework.model.response.DataResponse;
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
    public Mono<DataResponse<Customer>> createCustomer(@RequestBody CreateCustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest)
                .doOnError(throwable -> log.error("Error creating customer: {}", throwable.getMessage()));
    }

}
