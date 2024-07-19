package com.ezbuy.customerservice.api;

import com.ezbuy.customermodel.dto.CustomerDTO;
import com.ezbuy.customermodel.dto.request.AuthenticationRequest;
import com.ezbuy.customermodel.dto.request.CreateCustomerRequest;
import com.ezbuy.customermodel.dto.response.AuthenticationResponse;
import com.ezbuy.customerservice.domain.customer.service.CustomerService;
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
    public Mono<DataResponse<CustomerDTO>> createCustomer(@RequestBody CreateCustomerRequest customerRequest) {
        return customerService.createCustomer(customerRequest)
                .doOnError(throwable -> log.error("Error creating customer: {}", throwable.getMessage()));
    }

    @PostMapping("/auth")
    public Mono<DataResponse<AuthenticationResponse>> authenticateCustomer(@RequestBody AuthenticationRequest auth) {
        return customerService.authenticateRequest(auth)
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
