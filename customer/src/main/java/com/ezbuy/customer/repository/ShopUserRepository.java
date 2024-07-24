package com.ezbuy.customer.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.ezbuy.customer.model.postgresql.ShopUser;

public interface ShopUserRepository extends R2dbcRepository<ShopUser, Long> {}
