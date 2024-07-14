package com.ezbuy.customerservice.domain.address.repository;

import com.ezbuy.customermodel.model.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends R2dbcRepository<Address, Long> {

}
