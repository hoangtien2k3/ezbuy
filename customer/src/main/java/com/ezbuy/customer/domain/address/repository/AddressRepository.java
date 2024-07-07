package com.ezbuy.customer.domain.address.repository;

import com.ezbuy.customer.domain.address.entity.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends R2dbcRepository<Address, Integer> {

}
