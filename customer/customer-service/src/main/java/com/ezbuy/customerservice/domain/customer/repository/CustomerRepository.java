package com.ezbuy.customerservice.domain.customer.repository;

import com.ezbuy.customermodel.model.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, Long> {
}
