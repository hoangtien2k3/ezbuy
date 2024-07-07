package com.ezbuy.customer.service.impl;

import com.ezbuy.customer.constants.Constants;
import com.ezbuy.customer.dto.ValidateCustomerDto;
import com.ezbuy.customer.dto.request.CreateCustomerRequest;
import com.ezbuy.customer.dto.response.CustomerResponse;
import com.ezbuy.customer.model.Customer;
import com.ezbuy.customer.model.CustomerGroup;
import com.ezbuy.customer.repository.CustomerGroupRepository;
import com.ezbuy.customer.repository.CustomerRepository;
import com.ezbuy.customer.service.CustomerService;
import com.ezbuy.framework.exception.BusinessException;
import com.ezbuy.framework.model.response.DataResponse;
import com.ezbuy.framework.utils.DataUtil;
import com.ezbuy.framework.utils.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerGroupRepository customerGroupRepository;

    @Override
    public Mono<DataResponse<Customer>> createCustomer(CreateCustomerRequest customerRequest) {
        log.info("create customer: {}", customerRequest);
        return validRequestCreateCustomer(customerRequest)
                .flatMap(validate -> {
                    if (Boolean.FALSE.equals(validate.isValid())) {
                        return Mono.error(new BusinessException(String.valueOf(HttpStatus.BAD_REQUEST.value()), validate.errorMessage()));
                    }
                    // default customer group: NORMAL
                    var customerGroupMono = customerGroupRepository.findById(Constants.CUSTOMER_GROUP_CODE.NORMAL)
                            .switchIfEmpty(Mono.error(new BusinessException(String.valueOf(HttpStatus.NOT_FOUND.value()), Translator.toLocaleVi("customer.group.not.found"))))
                            .map(CustomerGroup::getId)
                            .doOnError(throwable -> log.error("customer group not found: {}", throwable.getMessage()));

                    return customerGroupMono.flatMap(customerGroupId -> {
                        Customer customer = Customer.builder()
                                .customerGroupId(customerGroupId)
                                .defaultAddressId(null)
                                .email(customerRequest.email())
                                .emailCanonical(customerRequest.email())
                                .firstName(customerRequest.firstName())
                                .lastName(customerRequest.lastName())
                                .birthday(null)
                                .gender(null)
                                .phoneNumber(null)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .subscribedToNewsletter(customerRequest.subscribedToNewsletter() != null ? customerRequest.subscribedToNewsletter() : false)
                                .build();

                        return customerRepository.save(customer)
                                .map(DataResponse::success);

                    });
                }).doOnError(throwable -> log.error("validate error: {}", throwable.getMessage()));
    }

    @Override
    public Mono<DataResponse<CustomerResponse>> getCustomerById(Integer customerId) {
        return null;
    }

    @Override
    public Mono<DataResponse<CustomerResponse>> updateCustomer(Integer customerId, CreateCustomerRequest customerRequest) {
        return null;
    }

    @Override
    public Mono<DataResponse<CustomerResponse>> deleteCustomer(Integer customerId) {
        return null;
    }

    @Override
    public Mono<DataResponse<CustomerResponse>> getCustomers() {
        return null;
    }

    private Mono<ValidateCustomerDto> validRequestCreateCustomer(CreateCustomerRequest customerRequest) {
        try {
            if (DataUtil.isNullOrEmpty(customerRequest.firstName())) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.not.null", "first.name")));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.lastName())) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.not.null", "last.name")));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.email())) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.not.null", "customer.email")));
            }
            if (DataUtil.isNullOrEmpty(customerRequest.password())) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.not.null", "customer.password")));
            }
            // validate email
            if (!customerRequest.email().matches(Constants.EMAIL_PATTERN)) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.invalid", "contact.email")));
            }
            // validata password
            if (!customerRequest.password().matches(Constants.PASSPORT_PATTERN)) {
                return Mono.just(new ValidateCustomerDto(false, Translator.toLocaleVi("customer.input.invalid", "customer.password")));
            }
            return Mono.just(new ValidateCustomerDto(true, ""));
        } catch (BusinessException e) {
            log.error("validate error: ", e);
            return Mono.just(new ValidateCustomerDto(false, e.getMessage()));
        }
    }

}
