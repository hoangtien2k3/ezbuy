package com.ezbuy.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.ezbuy.customer.model.postgresql.Address;

@Repository
public interface AddressRepository extends R2dbcRepository<Address, Long> {}
