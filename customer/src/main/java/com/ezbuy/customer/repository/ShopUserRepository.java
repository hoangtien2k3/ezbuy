package com.ezbuy.customer.repository;

import com.ezbuy.customer.model.postgresql.ShopUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ShopUserRepository extends R2dbcRepository<ShopUser, Long> {}
