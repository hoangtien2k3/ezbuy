package com.ezbuy.customer.repository;

import com.ezbuy.customer.model.postgresql.CustomerGroup;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerGroupRepository extends R2dbcRepository<CustomerGroup, Long> {}
