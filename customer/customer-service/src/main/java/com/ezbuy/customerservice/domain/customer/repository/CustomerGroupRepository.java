package com.ezbuy.customerservice.domain.customer.repository;

import com.ezbuy.customermodel.model.CustomerGroup;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CustomerGroupRepository extends R2dbcRepository<CustomerGroup, Long> {

}
