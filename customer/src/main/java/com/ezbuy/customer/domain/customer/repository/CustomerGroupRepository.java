package com.ezbuy.customer.domain.customer.repository;

import com.ezbuy.customer.domain.customer.entity.CustomerGroup;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerGroupRepository extends R2dbcRepository<CustomerGroup, Integer> {

}
