package com.ezbuy.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.customer.model.postgresql.CustomerGroup;

public interface CustomerGroupRepository extends R2dbcRepository<CustomerGroup, Long> {}
