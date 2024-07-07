package com.ezbuy.customer.domain.customer.repository;

import com.ezbuy.customer.domain.customer.entity.Customer;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerRepository extends R2dbcRepository<Customer, Integer> {
}
